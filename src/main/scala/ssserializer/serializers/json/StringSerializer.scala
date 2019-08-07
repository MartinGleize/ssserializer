package ssserializer.serializers.json
import org.apache.commons.text.StringEscapeUtils

import scala.reflect.runtime.universe

class StringSerializer extends StringWriterSerializer[String] {

  override def serialize(data: String, t: universe.Type): String = {
    StringEscapeUtils.escapeJson(data)
  }

}
