package ssserializer.deserializers

import scala.reflect.runtime.universe._

trait Deserializer[T, Input] {

  def deserialize(t: Type, src: Input, parentDeserializer: AnyDeserializer[Input]): T = {
    deserialize(t, src)
  }

  def deserialize(t: Type, src: Input): T
}