package ssserializer.serializers.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import ssserializer.serializers.{MasterSerializer, Serializer}

import scala.reflect.runtime.universe

class SeqSerializer extends Serializer {

  override def serialize(data: Any, t: universe.Type, dest: OutputStream, parentSerializer: MasterSerializer): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest))
    val elementType = t.typeArgs.head
    val seq = data.asInstanceOf[Seq[_]]
    val size = seq.size
    writer.write("[")
    for ((element, index) <- seq.zipWithIndex) {
      parentSerializer.serialize(element, elementType, dest)
      if (index != size - 1) {
        writer.write(",")
      }
    }
    writer.write("]")
    writer.flush()
  }
}
