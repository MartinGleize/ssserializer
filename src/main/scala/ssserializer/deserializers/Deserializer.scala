package ssserializer.deserializers

import scala.reflect.runtime.universe._

trait Deserializer[T, Input] {

  def deserialize(t: Type, src: Input, parentDeserializer: AnyDeserializer[Input] = null): T

}
