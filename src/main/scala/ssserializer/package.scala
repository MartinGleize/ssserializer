import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream, StringReader}

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

  def serialize[T : TypeTag](data: T)(implicit serializer: Serializer[OutputStream]): String = {
    val output = new ByteArrayOutputStream()
    serialize(data, output)
    output.close()
    output.toString("UTF-8")
  }

  /** Returns an object whose serialized representation is read on the provided input stream.
   * An implicit deserializer should be exposed, e.g. add the following import for a JSON representation:
   * import ssserializer.json._
   * */
  def deserialize[T : TypeTag](src: InputStream)(implicit deserializer: Deserializer[Any, InputStream]): T = {
    deserializer.deserialize(typeOf[T], src).asInstanceOf[T]
  }

  def deserialize[T : TypeTag](src: String)(implicit deserializer: Deserializer[Any, InputStream]): T = {
    val input = new ByteArrayInputStream(src.getBytes("UTF-8"))
    val res = deserialize[T](input)
    input.close()
    res
  }
}
