package ssserializer.serializers.json
import scala.reflect.runtime.universe._

class ToStringSerializer[T] extends StringWriterSerializer[T] {
  override def serialize(data: T, t: Type): String = String.valueOf(data)
}
