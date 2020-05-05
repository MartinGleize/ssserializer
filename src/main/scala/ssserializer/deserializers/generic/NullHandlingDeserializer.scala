package ssserializer.deserializers.generic

import scala.reflect.runtime.universe._
import ssserializer.deserializers.MasterDeserializer

/**
 * Overrides the Deserializer trait in the "deserializers" package with null-handling processing,
 * so that extending types can safely ignore to check for nullity.
 *
 * @tparam T a nullable type
 */
trait NullHandlingDeserializer[T, Input] extends ssserializer.deserializers.Deserializer[T, Input] {

  def hasReadNull(input: Input): Boolean

  def deserializeNonNull(t: Type, input: Input, parentDeserializer: MasterDeserializer[Input] = null): T

  override def deserialize(t: Type, input: Input, parentDeserializer: MasterDeserializer[Input] = null): T = {
    // first try to read a null value
    if (hasReadNull(input)) {
      null.asInstanceOf[T]
    } else {
      // not a null value, deserialize without worry about that
      deserializeNonNull(t, input, parentDeserializer)
    }
  }
}
