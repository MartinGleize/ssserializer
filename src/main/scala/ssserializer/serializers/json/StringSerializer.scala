package ssserializer.serializers.json
import org.apache.commons.text.StringEscapeUtils

import scala.reflect.runtime.universe

class StringSerializer extends StringWriterSerializer {

  override def serialize(data: Any, t: universe.Type): String = {
    StringEscapeUtils.escapeJson(data.asInstanceOf[String])
  }

}
