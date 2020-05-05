package ssserializer.deserializers.json

import ssserializer.deserializers.json.parsing.JsonReader

/**
 * Overrides the Deserializer trait in the "deserializers" package with JSON specific processing, such as handling of null.
 * @tparam T a nullable type
 */
trait NullHandlingDeserializer[T, JsonInput <: JsonReader] extends ssserializer.deserializers.generic.NullHandlingDeserializer[T, JsonInput] {

  override def hasReadNull(input: JsonInput): Boolean = {
    input.tryToConsumeToken(parsing.JsonReader.NULL)
  }

}
