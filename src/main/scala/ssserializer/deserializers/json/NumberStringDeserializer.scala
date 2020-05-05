package ssserializer.deserializers.json

import ssserializer.deserializers.json.parsing.JsonReader

trait NumberStringDeserializer[T, JsonInput <: JsonReader] extends StringParseDeserializer[T, JsonInput] {

  override def read(jsonReader: JsonInput): String = jsonReader.readJsonNumber()
}
