package ssserializer.deserializers.json

import ssserializer.deserializers.AnyDeserializer

import scala.reflect.runtime.universe

trait StringParseDeserializer[T] extends Deserializer[T] {

  override def deserializeNonNull(t: universe.Type, src: JsonReader, parentDeserializer: AnyDeserializer[JsonReader] = null): T = {
    val res = read(src)
    parse(res)
  }

  def read(jsonReader: JsonReader): String

  def parse(stringResult: String): T
}
