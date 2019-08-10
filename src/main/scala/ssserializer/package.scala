import java.io.OutputStream
import java.io.InputStream

import ssserializer.deserializers.MasterDeserializer
import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

package object ssserializer {

  def serialize[T : TypeTag](data: T, dest: OutputStream)(implicit serializer: MasterSerializer): Unit = ()

  def deserialize[T : TypeTag](src: InputStream)(implicit deserializer: MasterDeserializer[InputStream]): T = deserializer.deserialize(typeOf[T], src).asInstanceOf[T]

}
