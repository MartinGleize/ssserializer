package ssserializer.serializers.json
import scala.reflect.runtime.universe

class ToStringSerializer extends StringWriterSerializer {
  override def serialize(data: Any, t: universe.Type): String = String.valueOf(data)
}
