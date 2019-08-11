package ssserializer.serializers.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import scala.reflect.runtime.universe._
import ssserializer.serializers.{MasterSerializer, Serializer}

trait StringWriterSerializer extends Serializer {

  override def serialize(data: Any, t: Type, dest: OutputStream, parentSerializer: MasterSerializer): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest))
    writer.write(serialize(data, t))
    writer.flush()
  }

  def serialize(data: Any, t: Type): String
}
