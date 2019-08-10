package ssserializer.deserializers

import scala.reflect.runtime.universe._

/**
 * A deserializer.
 * @tparam T type of objects it produces
 * @tparam Input the serialized input (often a reader or a stream)
 */
trait Deserializer[T, Input] {

  def deserialize(t: Type, src: Input, parentDeserializer: MasterDeserializer[Input] = null): T

}
