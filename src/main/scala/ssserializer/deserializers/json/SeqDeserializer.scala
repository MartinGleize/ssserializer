package ssserializer.deserializers.json

import ssserializer.deserializers.AnyDeserializer

import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe

class SeqDeserializer extends Deserializer[Seq[_]] {

  override def deserializeNonNull(t: universe.Type, jsonReader: JsonReader, parentDeserializer: AnyDeserializer[JsonReader]): Seq[_] = {
    val elementType = t.typeArgs.head
    /*
    jsonReader.skipAfter(JsonReader.CURLY_OPEN)
    // read the size
    val sizeName = jsonReader.readJsonName() // TODO: check that it's "size"
    val sizeString = jsonReader.readJsonNumber()
    val size = sizeString.toInt
    jsonReader.skipAfter(JsonReader.p(","))
    val seqString = jsonReader.readJsonName() // check that it's "seq"
     */
    // TODO: handle null
    jsonReader.skipAfter(JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[Any]()
    /*
    for (i <- 1 to size) {
      val element = parentDeserializer.deserialize(elementType, jsonReader)
      res += element
      jsonReader.skipAfter(JsonReader.p(","))
    }
    */
    var potentialElement: Option[Any] = null
    while ({potentialElement = deserializeNextElement(elementType, jsonReader, parentDeserializer); potentialElement.isDefined}) {
      val element = potentialElement.get
      res += element
    }
    //jsonReader.skipAfter(JsonReader.CURLY_CLOSE)
    // TODO: this is where we would change things to construct objects of precise subclasses like List vs Vector
    res.toList
  }

  def deserializeNextElement(elementType: universe.Type, jsonReader: JsonReader, parentDeserializer: AnyDeserializer[JsonReader]): Option[Any] = {
    // TODO: handle null here
    // first try to look for the end of the JSON array
    if (jsonReader.tryToConsumeNextToken(JsonReader.BRACKET_CLOSE)) {
      None
    } else {
      // can't read a "]" so a new element can be read
      val element = parentDeserializer.deserialize(elementType, jsonReader)
      // try to read a "," after (if it fails it likely means this would be the last element)
      jsonReader.tryToConsumeNextToken(JsonReader.COMMA)
      Some(element)
    }
  }
}
