package ssserializer.serializers.json
import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

class OptionSerializer extends Serializer[Option[_]] {

  override def serializeNonNull(data: Option[_], t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    val elementType = t.typeArgs.head
    w.write("[")
    if (data.isDefined) {
      parentSerializer.serialize(data.get, elementType, w)
    }
    w.write("]")
  }

}
