import java.io.OutputStream
import java.io.InputStream

import ssserializer.deserializers.Deserializer
import ssserializer.serializers.AnySerializer

import scala.reflect.runtime.universe._

package object ssserializer {

  def serialize[T : TypeTag](data: T, dest: OutputStream)(implicit serializer: AnySerializer): Unit = ()

  def deserialize[T : TypeTag](src: InputStream)(implicit deserializer: Deserializer): T = null

}
