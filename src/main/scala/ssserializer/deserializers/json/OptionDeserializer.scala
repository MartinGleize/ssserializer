package ssserializer.deserializers.json
import ssserializer.deserializers.MasterDeserializer
import ssserializer.deserializers.json.parsing.JsonReader

import scala.reflect.runtime.universe._

class OptionDeserializer[JsonInput <: JsonReader] extends NullHandlingDeserializer[Option[_], JsonInput] {

  def buildFinalOption(element: Any, jsonInput: JsonInput): Option[_] = {
    Some(element)
  }

  override def deserializeNonNull(t: Type, jsonReader: JsonInput, parentDeserializer: MasterDeserializer[JsonInput]): Option[_] = {
    val elementType = t.typeArgs.head
    jsonReader.skipAfter(parsing.JsonReader.BRACKET_OPEN)
    if (jsonReader.tryToConsumeToken(parsing.JsonReader.BRACKET_CLOSE)) {
      None
    } else {
      val res = parentDeserializer.deserialize(elementType, jsonReader)
      jsonReader.skipAfter(parsing.JsonReader.BRACKET_CLOSE)
      buildFinalOption(res, jsonReader)
    }
  }

}
