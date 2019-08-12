package ssserializer.deserializers

import ssserializer.serializers.{MasterSerializer, Serializer}

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

/**
 * A deserializer.
 * @tparam T type of objects it produces
 * @tparam Input the serialized input (often a reader or a stream)
 */
trait Deserializer[T, Input] {

  def deserialize(t: Type, src: Input, parentDeserializer: MasterDeserializer[Input] = null): T

  /**
   * Convert to a deserializer for another type, using a conversion function between the 2 types
   * @param convert the conversion function between the intermediary object which this deserializer supports
   *                and the desired final output type
   * @tparam TargetT the type of the output of the new deserializer
   * @return a new deserializer for the second type
   */
  final def convertToDeserializer[TargetT : TypeTag](convert: T => TargetT): Deserializer[TargetT, Input] = {
    val baseDeserializer = this
    val targetBaseType = List(typeOf[TargetT].erasure.typeSymbol)
    def convertType(t: Type): Type = {
      t.substituteSymbols(targetBaseType, List(t.erasure.typeSymbol))
    }
    new Deserializer[TargetT, Input] {
      override def deserialize(t: Type, src: Input, parentDeserializer: MasterDeserializer[Input]): TargetT = {
        val sourceObject = baseDeserializer.deserialize(convertType(t), src, parentDeserializer)
        if (sourceObject == null) null.asInstanceOf[TargetT] else convert(sourceObject)
      }
    }
  }
}
