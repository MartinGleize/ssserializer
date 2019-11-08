package ssserializer.serializers.json

import ssserializer.deserializers.json.parsing.JsonUtil

import scala.reflect.runtime.universe

class StringSerializer extends StringWriterSerializer[String] {

  override def serialize(data: String, t: universe.Type): String = {
    "\"" + JsonUtil.escape(data) + "\""
  }

}
