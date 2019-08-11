package ssserializer.serializers.json

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

class MapSerializer extends Serializer[Map[_, _]] {

  override def serializeNonNull(map: Map[_, _], t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    w.write("[")
    val keyType = t.typeArgs(0)
    val valueType = t.typeArgs(1)
    val size = map.size
    for (((key, value), index) <- map.zipWithIndex) {
      w.write("{\"key\":")
      parentSerializer.serialize(key, keyType, w)
      w.write(",\"value\":")
      parentSerializer.serialize(value, valueType, w)
      w.write("}")
      if (index != size - 1) {
        w.write(",")
      }
    }
    w.write("]")
  }

}
