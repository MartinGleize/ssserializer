package ssserializer.deserializers

package object json {

  val doubleDeserializer: NumberStringDeserializer[Double] = _.toDouble

  val longDeserializer: NumberStringDeserializer[Long] = _.toLong

  val intDeserializer: NumberStringDeserializer[Int] = _.toInt

  val booleanDeserializer: StringParseDeserializer[Boolean] = new StringParseDeserializer[Boolean] {
    override def read(jsonReader: JsonReader): String = jsonReader.readJsonBoolean()

    override def parse(stringResult: String): Boolean = stringResult.toBoolean
  }
}
