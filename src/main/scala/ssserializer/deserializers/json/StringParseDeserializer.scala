package ssserializer.deserializers.json

import ssserializer.deserializers.Deserializer

import scala.reflect.runtime.universe

trait StringParseDeserializer[T] extends Deserializer[T, JsonReader] {

  override def deserialize(t: universe.Type, src: JsonReader): T = {
    val res = read(src)
    parse(res)
  }

  def read(jsonReader: JsonReader): String

  def parse(stringResult: String): T
}
