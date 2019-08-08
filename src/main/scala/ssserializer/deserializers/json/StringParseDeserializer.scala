package ssserializer.deserializers.json

import java.io.{InputStream, InputStreamReader}

import ssserializer.deserializers.Deserializer

import scala.reflect.runtime.universe

trait StringParseDeserializer[T] extends Deserializer[T] {

  override def deserialize(t: universe.Type, src: InputStream): T = {
    val jsonReader = new JsonReader(new InputStreamReader(src))
    val res = read(jsonReader)
    parse(res)
  }

  def read(jsonReader: JsonReader): String

  def parse(stringResult: String): T
}
