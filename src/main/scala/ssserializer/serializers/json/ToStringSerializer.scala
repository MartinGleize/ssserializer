package ssserializer.serializers.json
import scala.reflect.runtime.universe

class ToStringSerializer[T] extends StringWriterSerializer[T] {
  override def serialize(data: T, t: universe.Type): String = String.valueOf(data)
}
