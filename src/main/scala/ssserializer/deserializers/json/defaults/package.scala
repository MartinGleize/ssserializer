package ssserializer.deserializers.json

import ssserializer.deserializers.json.parsing.JsonReader

package object defaults {

  def doubleDeserializer[JsonInput <: JsonReader]: NumberStringDeserializer[Double, JsonInput] = _.toDouble

  def longDeserializer[JsonInput <: JsonReader]: NumberStringDeserializer[Long, JsonInput] = _.toLong

  def intDeserializer[JsonInput <: JsonReader]: NumberStringDeserializer[Int, JsonInput] = _.toInt

  def booleanDeserializer[JsonInput <: JsonReader]: StringParseDeserializer[Boolean, JsonInput] = {
    new StringParseDeserializer[Boolean, JsonInput] {
      override def read(jsonReader: JsonInput): String = jsonReader.readJsonBoolean()

      override def parse(stringResult: String): Boolean = stringResult.toBoolean
    }
  }

  def stringDeserializer[JsonInput <: JsonReader]: StringParseDeserializer[String, JsonInput] = {
    new StringDeserializer()
  }

  val seqDeserializer: SeqDeserializer[Seq[_], JsonReader] = (elts, _, _) => elts

  val setDeserializer: SeqDeserializer[Set[_], JsonReader] = (elts, _, _) => elts.toSet

  val arrayDeserializer: ssserializer.deserializers.Deserializer[Array[_], JsonReader] =
    seqDeserializer.convertToDeserializer[Array[_]](_.toArray)
}
