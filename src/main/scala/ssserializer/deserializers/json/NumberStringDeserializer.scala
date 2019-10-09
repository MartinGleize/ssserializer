package ssserializer.deserializers.json

import ssserializer.deserializers.json.parsing.JsonReader

trait NumberStringDeserializer[T <: AnyVal] extends StringParseDeserializer[T] {

  override def read(jsonReader: JsonReader): String = jsonReader.readJsonNumber()
}
