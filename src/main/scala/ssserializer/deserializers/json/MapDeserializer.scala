package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer
import ssserializer.deserializers.json.parsing.JsonReader

import scala.collection.immutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

class MapDeserializer[JsonInput <: JsonReader] extends NullHandlingDeserializer[Map[_, _], JsonInput] {

  def buildFinalMap(keyValuePairs: Seq[(Any, Any)], jsonReader: JsonInput): Map[Any, Any] = {
    immutable.Map(keyValuePairs:_*)
  }

  override def deserializeNonNull(t: Type, jsonReader: JsonInput, parentDeserializer: MasterDeserializer[JsonInput]): Map[_, _] = {
    val keyType = t.typeArgs(0)
    val valueType = t.typeArgs(1)
    jsonReader.skipAfter(parsing.JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[(Any, Any)]()
    var potentialElement: Option[(Any, Any)] = null
    while ({potentialElement = deserializeNextKeyValuePair(keyType, valueType, jsonReader, parentDeserializer); potentialElement.isDefined}) {
      val element = potentialElement.get
      res += element
    }
    buildFinalMap(res, jsonReader)
  }

  def deserializeNextKeyValuePair(keyType: Type, valueType: Type, jsonReader: JsonInput, parentDeserializer: MasterDeserializer[JsonInput]): Option[(Any, Any)] = {
    // first try to look for the end of the JSON array
    if (jsonReader.tryToConsumeToken(parsing.JsonReader.BRACKET_CLOSE)) {
      None
    } else {
      // can't read a "]" so a new element can be read
      jsonReader.skipAfter(parsing.JsonReader.CURLY_OPEN)
      // read the key (ignore the JSON name, should be "k")
      jsonReader.readJsonName()
      val key = parentDeserializer.deserialize(keyType, jsonReader)
      jsonReader.skipAfter(parsing.JsonReader.COMMA)
      // read the value (ignore the JSON name again, should be "v")
      jsonReader.readJsonName()
      val value = parentDeserializer.deserialize(valueType, jsonReader)
      jsonReader.skipAfter(parsing.JsonReader.CURLY_CLOSE)
      // try to read a "," after (if it fails it likely means this would be the last element)
      jsonReader.tryToConsumeToken(parsing.JsonReader.COMMA)
      Some((key, value))
    }
  }

}
