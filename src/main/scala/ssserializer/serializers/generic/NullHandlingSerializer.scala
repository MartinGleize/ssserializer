package ssserializer.serializers.generic

import ssserializer.serializers.{MasterSerializer, Serializer}

import scala.reflect.runtime.universe._

/**
 * Overrides the Serializer trait in the "serializers" package with JSON specific processing, such as handling of null.
 * @tparam T the type of object to serialize (can be nullable or not)
 */
trait NullHandlingSerializer[T, Output] extends Serializer[Output] {

  def serializeNonNull(data: T, t: Type, output: Output, parentSerializer: MasterSerializer[Output]): Unit

  def outputNull(output: Output): Unit

  final override def serialize(data: Any, t: Type, output: Output, parentSerializer: MasterSerializer[Output]): Unit = {
    // if the value is null, write it as a JSON null
    if (data == null) {
      outputNull(output)
    }
    else {
      // not a null value, serialize without worrying about this case
      val castedData = data.asInstanceOf[T]
      serializeNonNull(castedData, t, output, parentSerializer)
    }
  }

}
