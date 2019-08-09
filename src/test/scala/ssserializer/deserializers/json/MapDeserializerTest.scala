package ssserializer.deserializers.json

class MapDeserializerTest extends JsonDeserializerSpec {

  "A JSON MapDeserializer" should "deserialize JSON key/value pairs correctly into a map (with simple child types)" in {
    val checks: Seq[(String, Map[String, Int])] = Seq(
      "[]" -> Map(),
      "[ {\"key\":\"key1\", \"value\":1}, {\"key\":\"key2\", \"value\":2}]" -> Map("key1" -> 1, "key2" -> 2),
      "   null   " -> null
    )
    checks.foreach { case (input, output) =>
      assertEncodedStringSameAsDeserializedValue(input, output)
    }
  }
}
