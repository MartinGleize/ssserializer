package ssserializer.deserializers.json

import org.scalatest.{FlatSpec, Matchers}
import ssserializer.deserializers.json.parsing.JsonReader

trait JsonReaderTest extends FlatSpec with Matchers {

  def jsonReader(input: String): JsonReader

  "A JsonReader" should "read JSON strings correctly" in {
    val inputOutputs = Seq(
      "\"lol\" hohoho" -> "lol",
      "     \"lol\\\"lol\" hohoho" -> "lol\"lol",
      "\"\" oops" -> ""
    )
    inputOutputs.foreach { case (input, output) =>
      jsonReader(input).readJsonString() should be (parsing.JsonReader.quote(output))
    }
  }

  it should "read JSON name/value pairs correctly" in {
    val objects: Seq[(String, parsing.JsonReader => String, (String, String))] = Seq(
      ("{\"key1\":3.1416}", _.readJsonNumber(), ("key1", "3.1416")),
      ("{\"key2\" : 0.2736}", _.readJsonNumber(), ("key2", "0.2736")),
      ("{      \"key3\":128712          }", _.readJsonNumber(), ("key3", "128712")),
      ("{\"key4\":true }", _.readJsonBoolean(), ("key4", "true")),
      ("{\"key5\": false }", _.readJsonBoolean(), ("key5", "false"))
    )
    objects.foreach { case (input, method, (name, value)) =>
      readNameValue(jsonReader(input), method) should be ((parsing.JsonReader.quote(name), value))
    }
  }

  def readNameValue(reader: parsing.JsonReader, method: parsing.JsonReader => String): (String, String) = {
    reader.skipAfter(JsonReader.CURLY_OPEN)
    val res = (reader.readJsonName(), method(reader))
    reader.skipAfter(JsonReader.CURLY_CLOSE)
    res
  }
}
