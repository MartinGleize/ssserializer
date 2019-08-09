package ssserializer.deserializers.json

class SeqDeserializerTest extends JsonDeserializerSpec {

  "A JSON SeqDeserializer" should "deserialize JSON sequences correctly (with simple parameter types)" in {
    val checksInt: Seq[(String, Seq[Int])] = Seq(
      " [1,2]" -> Seq(1, 2),
      "[1,2, 3]" -> Seq(1, 2 ,3),
      "null" -> null
    )
    val checksBoolean = Seq(
      "[true, false, false]" -> Seq(true, false, false)
    )
    checksInt.foreach { case (input, output) =>
      assertEncodedStringSameAsDeserializedValue(input, output)
    }
    checksBoolean.foreach { case (input, output) =>
      assertEncodedStringSameAsDeserializedValue(input, output)
    }
  }
}
