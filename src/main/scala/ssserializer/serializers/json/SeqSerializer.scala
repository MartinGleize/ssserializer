package ssserializer.serializers.json

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

class SeqSerializer[S <: Seq[_]] extends Serializer[S] {

  override def serializeNonNull(seq: S, t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
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

  /*
  def toSerializer[TargetT : TypeTag](convert: S => TargetT): Serializer[S] = {
    (data, t, dest, parentSerializer) => {
      val convertedData = convert(data)
      this.serialize(convertedData, typeOf[TargetT], dest, parentSerializer)
    }
  }
  */
}
