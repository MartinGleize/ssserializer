package ssserializer.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets

import ssserializer.serializers.{MasterSerializer, Serializer}

import scala.reflect.runtime.universe._

class JsonSerializer extends Serializer[OutputStream] {

  val serializer: MasterSerializer[BufferedWriter] = new ssserializer.serializers.json.JsonSerializer()

  override def serialize(data: Any, t: Type, dest: OutputStream, parentSerializer: MasterSerializer[OutputStream]): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest, StandardCharsets.UTF_8))
    serializer.serialize(data, t, writer)
    writer.close()
  }

}
