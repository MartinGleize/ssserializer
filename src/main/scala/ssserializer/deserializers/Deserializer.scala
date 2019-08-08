package ssserializer.deserializers

import java.io.InputStream
import scala.reflect.runtime.universe._

trait Deserializer[T] {

  def deserialize(t: Type, src: InputStream, parentDeserializer: AnyDeserializer): T = {
    deserialize(t, src)
  }

  def deserialize(t: Type, src: InputStream): T
}
