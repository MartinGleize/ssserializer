package ssserializer.serializers.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import ssserializer.serializers.{MasterSerializer, Serializer}

import scala.reflect.runtime.universe

class MapSerializer extends Serializer {

  override def serialize(data: Any, t: universe.Type, dest: OutputStream, parentSerializer: MasterSerializer): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest))
    writer.write("[")
    val keyType = t.typeArgs(0)
    val valueType = t.typeArgs(1)
    val map = data.asInstanceOf[Map[_, _]]
    val size = map.size
    for (((key, value), index) <- map.zipWithIndex) {
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

}
