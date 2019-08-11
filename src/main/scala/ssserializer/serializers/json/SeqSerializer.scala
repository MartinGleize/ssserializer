package ssserializer.serializers.json

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

class SeqSerializer extends Serializer[Seq[_]] {

  override def serializeNonNull(seq: Seq[_], t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    val elementType = t.typeArgs.head
    val size = seq.size
    w.write("[")
    for ((element, index) <- seq.zipWithIndex) {
      parentSerializer.serialize(element, elementType, w)
      if (index != size - 1) {
        w.write(",")
      }
    }
    w.write("]")
  }
}
