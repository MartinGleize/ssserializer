package ssserializer.deserializers.json
import ssserializer.deserializers.json.parsing._

class ReallyFastJsonReaderTest extends JsonReaderTest {
  override def jsonReader(input: String): JsonReader = {
    new ReallyFastJsonReader(input)
  }
}
