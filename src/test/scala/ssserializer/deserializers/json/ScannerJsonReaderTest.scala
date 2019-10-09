package ssserializer.deserializers.json
import ssserializer.deserializers.json.parsing.{JsonReader, ScannerJsonReader}

class ScannerJsonReaderTest extends JsonReaderTest {
  override def jsonReader(input: String): JsonReader = {
    new ScannerJsonReader(input)
  }
}
