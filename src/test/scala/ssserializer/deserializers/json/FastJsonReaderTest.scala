package ssserializer.deserializers.json
import ssserializer.deserializers.json.parsing._

class FastJsonReaderTest extends JsonReaderTest {
  override def jsonReader(input: String): JsonReader = {
    new FastJsonReader(input)
  }
}
