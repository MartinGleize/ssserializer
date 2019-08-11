package ssserializer.serializers.json

import java.io.BufferedWriter

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

/**
 * Overrides the Serializer trait in the "serializers" package with JSON specific processing, such as handling of null.
 * @tparam T the type of object to serialize (can be nullable or not)
 */
trait Serializer[T] extends ssserializer.serializers.Serializer[BufferedWriter] {

  type Writer = BufferedWriter

  def serializeNonNull(data: T, t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit

  final override def serialize(data: Any, t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    // if the value is null, write it as a JSON null
    if (data == null) {
      w.write("null")
    }
    else {
      // not a null value, serialize without worry about that
      val castedData = data.asInstanceOf[T]
      serializeNonNull(castedData, t, w, parentSerializer)
    }
    w.flush()
  }

}
