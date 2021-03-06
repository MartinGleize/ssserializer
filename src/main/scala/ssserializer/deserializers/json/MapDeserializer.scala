package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer

import scala.collection.immutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

class MapDeserializer extends Deserializer[Map[_, _]] {

  override def deserializeNonNull(t: Type, jsonReader: parsing.JsonReader, parentDeserializer: MasterDeserializer[parsing.JsonReader]): Map[_, _] = {
    val keyType = t.typeArgs(0)
    val valueType = t.typeArgs(1)
    jsonReader.skipAfter(parsing.JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[(Any, Any)]()
    var potentialElement: Option[(Any, Any)] = null
    while ({potentialElement = deserializeNextKeyValuePair(keyType, valueType, jsonReader, parentDeserializer); potentialElement.isDefined}) {
      val element = potentialElement.get
      res += element
    }
    // TODO: this is where we would change things to construct objects of precise subtypes like HashMap vs TreeMap vs SortedMap
    immutable.Map(res.toSeq:_*)
  }

  def deserializeNextKeyValuePair(keyType: Type, valueType: Type, jsonReader: parsing.JsonReader, parentDeserializer: MasterDeserializer[parsing.JsonReader]): Option[(Any, Any)] = {
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
