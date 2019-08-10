package ssserializer.serializers

import java.io.OutputStream
import scala.reflect.runtime.universe._

trait Serializer[T] {

  def serialize(data: T, t: Type, dest: OutputStream, parentSerializer: MasterSerializer): Unit = {
    serialize(data, t, dest)
  }

  def serialize(data: T, t: Type, dest: OutputStream): Unit
}
