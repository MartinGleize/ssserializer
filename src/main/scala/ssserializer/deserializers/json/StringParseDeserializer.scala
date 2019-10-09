package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer

import scala.reflect.runtime.universe

trait StringParseDeserializer[T] extends Deserializer[T] {

  override def deserializeNonNull(t: universe.Type, src: parsing.JsonReader, parentDeserializer: MasterDeserializer[parsing.JsonReader] = null): T = {
    val res = read(src)
    parse(res)
  }

  def read(jsonReader: parsing.JsonReader): String

  def parse(stringResult: String): T
}
