import java.io.OutputStream
import java.io.InputStream

import ssserializer.deserializers.Deserializer
import ssserializer.serializers.Serializer

import scala.reflect.runtime.universe._

package object ssserializer {

  def serialize[T : TypeTag](data: T, dest: OutputStream)(implicit serializer: Serializer): Unit = ()

  def serialize[T : TypeTag](data: T)(implicit serializer: Serializer): String = ""

  def deserialize[T : TypeTag](input: InputStream)(implicit deserializer: Deserializer): T = null

  def deserialize[T : TypeTag](input: String)(implicit deserializer: Deserializer): T = null

}
