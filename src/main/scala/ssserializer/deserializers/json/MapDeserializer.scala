package ssserializer.deserializers.json

import ssserializer.deserializers.AnyDeserializer

import scala.collection.immutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

class MapDeserializer extends Deserializer[Map[_, _]] {

  override def deserializeNonNull(t: Type, jsonReader: JsonReader, parentDeserializer: AnyDeserializer[JsonReader]): Map[_, _] = {
    val keyType = t.typeArgs(0)
    val valueType = t.typeArgs(1)
    // TODO: handle null
    jsonReader.skipAfter(JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[(Any, Any)]()
    var potentialElement: Option[(Any, Any)] = null
    while ({potentialElement = deserializeNextKeyValuePair(keyType, valueType, jsonReader, parentDeserializer); potentialElement.isDefined}) {
      val element = potentialElement.get
      res += element
    }
    // TODO: this is where we would change things to construct objects of precise subtypes like HashMap vs TreeMap vs SortedMap
    immutable.Map(res.toSeq:_*)
  }

  def deserializeNextKeyValuePair(keyType: Type, valueType: Type, jsonReader: JsonReader, parentDeserializer: AnyDeserializer[JsonReader]): Option[(Any, Any)] = {
    // TODO: handle null here
    // first try to look for the end of the JSON array
    if (jsonReader.tryToConsumeNextToken(JsonReader.BRACKET_CLOSE)) {
      None
    } else {
      // can't read a "]" so a new element can be read
      jsonReader.skipAfter(JsonReader.CURLY_OPEN)
      // read the key (ignore the JSON name, should be "key")
      jsonReader.readJsonName()
      val key = parentDeserializer.deserialize(keyType, jsonReader)
      jsonReader.skipAfter(JsonReader.COMMA)
      // read the value (ignore the JSON name again, should be "value")
      jsonReader.readJsonName()
      val value = parentDeserializer.deserialize(valueType, jsonReader)
      jsonReader.skipAfter(JsonReader.CURLY_CLOSE)
      // try to read a "," after (if it fails it likely means this would be the last element)
      jsonReader.tryToConsumeNextToken(JsonReader.COMMA)
      Some((key, value))
    }
  }

}
