package ssserializer.serializers.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import scala.reflect.runtime.universe._
import ssserializer.serializers.Serializer

trait StringWriterSerializer[T] extends Serializer[T] {

  override def serialize(data: T, t: Type, dest: OutputStream): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest))
    writer.write(serialize(data, t))
    writer.flush()
  }

  def serialize(data: T, t: Type): String
}
