package ssserializer.deserializers.json
import ssserializer.deserializers.MasterDeserializer

import scala.reflect.runtime.universe._

class OptionDeserializer extends Deserializer[Option[_]] {

  override def deserializeNonNull(t: Type, jsonReader: JsonReader, parentDeserializer: MasterDeserializer[JsonReader]): Option[_] = {
    val elementType = t.typeArgs.head
    jsonReader.skipAfter(JsonReader.BRACKET_OPEN)
    if (jsonReader.tryToConsumeNextToken(JsonReader.BRACKET_CLOSE)) {
      None
    } else {
      val res = parentDeserializer.deserialize(elementType, jsonReader)
      jsonReader.skipAfter(JsonReader.BRACKET_CLOSE)
      Some(res)
    }
  }

}
