package ssserializer.deserializers.json
import ssserializer.deserializers.json.parsing.{FastJsonReader, JsonReader}

class FastJsonReaderTest extends JsonReaderTest {
  override def jsonReader(input: String): JsonReader = {
    new FastJsonReader(input)
  }
}
