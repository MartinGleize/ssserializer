package ssserializer.serializers.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import ssserializer.serializers.{AnySerializer, Serializer}

import scala.reflect.runtime.universe

class SeqSerializer extends Serializer[Seq[_]] {

  override def serialize(data: Seq[_], t: universe.Type, dest: OutputStream, parentSerializer: AnySerializer): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest))
    val elementType = t.typeArgs.head
    val size = data.size
    writer.write("{\"size\":" + size + ",\"seq\":")
    writer.write("[")
    for ((element, index) <- data.zipWithIndex) {
      parentSerializer.serialize(element, elementType, dest)
      if (index != size - 1) {
        writer.write(",")
      }
    }
    writer.write("]")
    writer.write("}")
    writer.flush()
  }

  override def serialize(data: Seq[_], t: universe.Type, dest: OutputStream): Unit = () // TODO: throw some custom exception
}
