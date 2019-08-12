package ssserializer.serializers

import scala.reflect.runtime.universe._

/**
 * A serializer.
 * @tparam Output the destination for the serialized object (often a writer or an output stream)
 */
trait Serializer[Output] {

  def serialize(data: Any, t: Type, dest: Output, parentSerializer: MasterSerializer[Output] = null): Unit

  final def serialize[T : TypeTag](data: T, dest: Output): Unit = {
    // in effect, this is type erasure
    serialize(data, typeOf[T], dest)
  }

  /**
   * Convert to a serializer supporting another type, using a conversion function between the 2 types
   * @param convert the conversion function from the input object to the intermediary-type object supported by this serializer
   * @tparam SourceT the type of the object to serialize
   * @tparam TargetT the type of intermediary object which this serializer supports
   * @return a new serializer for the second type
   */
  final def convertToSerializer[SourceT : TypeTag, TargetT : TypeTag](convert: SourceT => TargetT): Serializer[Output] = {
    val baseSerializer = this
    val sourceBaseType = List(typeOf[SourceT].erasure.typeSymbol)
    val targetBaseType = List(typeOf[TargetT].erasure.typeSymbol)
    def convertType(t: Type): Type = {
      t.substituteSymbols(sourceBaseType, targetBaseType)
    }
    new Serializer[Output] {
      override def serialize(data: Any, t: Type, dest: Output, parentSerializer: MasterSerializer[Output]): Unit = {
        val convertedData = if (data == null) null else convert(data.asInstanceOf[SourceT])
        baseSerializer.serialize(convertedData, convertType(t), dest, parentSerializer)
      }
    }
  }
}
