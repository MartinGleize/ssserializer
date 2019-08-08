package ssserializer.deserializers.json

package object defaults {

  val doubleDeserializer: NumberStringDeserializer[Double] = _.toDouble

  val longDeserializer: NumberStringDeserializer[Long] = _.toLong

  val intDeserializer: NumberStringDeserializer[Int] = _.toInt

  val booleanDeserializer: StringParseDeserializer[Boolean] = new StringParseDeserializer[Boolean] {
    override def read(jsonReader: JsonReader): String = jsonReader.readJsonBoolean()

    override def parse(stringResult: String): Boolean = stringResult.toBoolean
  }

  val stringDeserializer: StringParseDeserializer[String] = new StringParseDeserializer[String] {
    override def read(jsonReader: JsonReader): String = jsonReader.readJsonString()

    override def parse(stringResult: String): String = stringResult
  }

}
