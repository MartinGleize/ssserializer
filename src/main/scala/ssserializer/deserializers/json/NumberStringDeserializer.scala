package ssserializer.deserializers.json

trait NumberStringDeserializer[T <: AnyVal] extends StringParseDeserializer[T] {

  override def read(jsonReader: JsonReader): String = jsonReader.readJsonNumber()
}
