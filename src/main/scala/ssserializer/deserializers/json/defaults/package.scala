package ssserializer.deserializers.json

package object defaults {

  val doubleDeserializer: NumberStringDeserializer[Double] = _.toDouble

  val longDeserializer: NumberStringDeserializer[Long] = _.toLong

  val intDeserializer: NumberStringDeserializer[Int] = _.toInt

  val booleanDeserializer: StringParseDeserializer[Boolean] = new StringParseDeserializer[Boolean] {
    override def read(jsonReader: parsing.JsonReader): String = jsonReader.readJsonBoolean()

    override def parse(stringResult: String): Boolean = stringResult.toBoolean
  }

  val stringDeserializer: StringParseDeserializer[String] = new StringDeserializer()

  val seqDeserializer: SeqDeserializer[Seq[_]] = _.toList

  val setDeserializer: SeqDeserializer[Set[_]] = _.toSet

  val arrayDeserializer: ssserializer.deserializers.Deserializer[Array[_], parsing.JsonReader] = seqDeserializer.convertToDeserializer[Array[_]](_.toArray)
}
