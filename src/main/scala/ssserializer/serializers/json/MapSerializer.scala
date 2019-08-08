package ssserializer.serializers.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import ssserializer.serializers.{AnySerializer, Serializer}

import scala.reflect.runtime.universe

class MapSerializer extends Serializer[Map[_, _]] {

  override def serialize(data: Map[_, _], t: universe.Type, dest: OutputStream, parentSerializer: AnySerializer): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest))
    writer.write("[")
    val keyType = t.typeArgs(0)
    val valueType = t.typeArgs(1)
    val size = data.size
    for (((key, value), index) <- data.zipWithIndex) {
      writer.write("{\"key\":")
      parentSerializer.serialize(key, keyType, dest)
      writer.write(",\"value\":")
      parentSerializer.serialize(value, valueType, dest)
      writer.write("}")
      if (index != size - 1) {
        writer.write(",")
      }
    }
    writer.write("]")
    writer.flush()
  }

  override def serialize(data: Map[_, _], t: universe.Type, dest: OutputStream): Unit = () // TODO: throw some custom exception

}
