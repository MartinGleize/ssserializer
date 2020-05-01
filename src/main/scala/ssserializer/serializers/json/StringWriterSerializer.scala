package ssserializer.serializers.json

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

trait StringWriterSerializer[T] extends NullHandlingSerializer[T] {

  def serialize(data: T, t: Type): String

  final override def serializeNonNull(data: T, t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    w.write(serialize(data, t))
  }
}
