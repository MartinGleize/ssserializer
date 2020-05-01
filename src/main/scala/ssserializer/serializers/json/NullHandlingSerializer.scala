package ssserializer.serializers.json

import java.io.BufferedWriter

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

/**
 * Overrides the Serializer trait in the "serializers" package with JSON specific processing, such as handling of null.
 * @tparam T the type of object to serialize (can be nullable or not)
 */
trait NullHandlingSerializer[T] extends ssserializer.serializers.generic.NullHandlingSerializer[T, BufferedWriter] {

  type Writer = BufferedWriter

  override def outputNull(w: BufferedWriter): Unit = {
    w.write("null")
  }

}
