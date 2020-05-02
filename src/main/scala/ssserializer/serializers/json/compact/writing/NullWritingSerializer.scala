package ssserializer.serializers.json.compact.writing

import ssserializer.serializers.MasterSerializer
import ssserializer.serializers.json.NullHandlingSerializer

import scala.reflect.runtime.universe

class NullWritingSerializer extends NullHandlingSerializer[Any] {

  override def serializeNonNull(data: Any, t: universe.Type, output: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    // this means 'data' wasn't actually null so the serializer couldn't handle the type
    throw new RuntimeException("Writing of type not handled: " + t)
  }

}
