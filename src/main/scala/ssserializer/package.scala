import java.io.OutputStream
import java.io.InputStream

import ssserializer.deserializers.Deserializer
import ssserializer.serializers.Serializer

import scala.reflect.runtime.universe._

/**
 * ssserializer, the Simple Scala Serializer, is a library for simple serialization/deserialization for Scala.
 * Contrary to alternatives, it focuses on ease of use at compile-time, requiring virtually no extra boilerplate code
 * to add to the structures you want to serialize, and allowing a lot of flexibility with common types.
 *
 * @author Martin Gleize
 */
package object ssserializer {

  /** Writes a serialized representation of the data to an output stream.
   * An implicit serializer should be exposed, e.g. add the following import for a JSON representation:
   * import ssserializer.json._
   * */
  def serialize[T : TypeTag](data: T, dest: OutputStream)(implicit serializer: Serializer[OutputStream]): Unit = {
    serializer.serialize(data, typeOf[T], dest)
  }

  /** Returns an object whose serialized representation is read on the provided input stream.
   * An implicit deserializer should be exposed, e.g. add the following import for a JSON representation:
   * import ssserializer.json._
   * */
  def deserialize[T : TypeTag](src: InputStream)(implicit deserializer: Deserializer[Any, InputStream]): T = {
    deserializer.deserialize(typeOf[T], src).asInstanceOf[T]
  }

}
