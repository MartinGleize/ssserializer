package ssserializer.deserializers.json

import org.scalatest.{FlatSpec, Matchers}

class JsonReaderTest extends FlatSpec with Matchers {

  "A JsonReader" should "read JSON strings correctly" in {
    val inputOutputs = Seq(
      "\"lol\" hohoho" -> "lol",
      "     \"lol\\\"lol\" hohoho" -> "lol\"lol",
      "sldfjlk sd\"\" oops" -> ""
    )
    inputOutputs.foreach { case (input, output) =>
      new JsonReader(input).readJsonString() should be (output)
    }
  }

  it should "read JSON name/value pairs correctly" in {
    val nameValues: Seq[(String, JsonReader => String, (String, String))] = Seq(
      ("\"key1\":3.1416", _.readJsonNumber(), ("key1", "3.1416")),
      ("\"key2\" : 0.2736", _.readJsonNumber(), ("key2", "0.2736")),
      ("      \"key3\":128712          ", _.readJsonNumber(), ("key3", "128712")),
      ("\"key4\":true ", _.readJsonBoolean(), ("key4", "true")),
      ("\"key5\": false ", _.readJsonBoolean(), ("key5", "false"))
    )
    nameValues.foreach { case (input, method, (name, value)) =>
      readNameValue(new JsonReader(input), method) should be ((name, value))
    }
  }

  def readNameValue(reader: JsonReader, method: JsonReader => String): (String, String) = {
    (reader.readJsonName(), method(reader))
  }
}
