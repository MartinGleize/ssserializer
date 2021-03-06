package ssserializer.deserializers.json

import scala.reflect.runtime.universe._
import ssserializer.deserializers.MasterDeserializer

/**
 * Overrides the Deserializer trait in the "deserializers" package with JSON specific processing, such as handling of null.
 * @tparam T a nullable type
 */
trait Deserializer[T] extends ssserializer.deserializers.Deserializer[T, parsing.JsonReader] {

  def deserializeNonNull(t: Type, jsonReader: parsing.JsonReader, parentDeserializer: MasterDeserializer[parsing.JsonReader] = null): T

  override def deserialize(t: Type, jsonReader: parsing.JsonReader, parentDeserializer: MasterDeserializer[parsing.JsonReader] = null): T = {
    // first try to read a null value
    if (jsonReader.tryToConsumeToken(parsing.JsonReader.NULL)) {
      // TODO: check that this is not TOO dirty, this code branch shouldn't be reachable on non-nullable types
      null.asInstanceOf[T]
    } else {
      // not a null value, deserialize without worry about that
      deserializeNonNull(t, jsonReader, parentDeserializer)
    }
  }
}
