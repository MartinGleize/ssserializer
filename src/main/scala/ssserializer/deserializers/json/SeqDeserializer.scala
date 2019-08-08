package ssserializer.deserializers.json

import ssserializer.deserializers.{AnyDeserializer, Deserializer}

import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe

class SeqDeserializer extends Deserializer[Seq[_], JsonReader] {

  override def deserialize(t: universe.Type, jsonReader: JsonReader, parentDeserializer: AnyDeserializer[JsonReader]): Seq[_] = {
    val elementType = t.typeArgs.head
    // TODO: handle null
    jsonReader.skipAfter(JsonReader.CURLY_OPEN)
    // read the size
    val sizeName = jsonReader.readJsonName() // TODO: check that it's "size"
    val sizeString = jsonReader.readJsonNumber()
    val size = sizeString.toInt
    jsonReader.skipAfter(JsonReader.p(","))
    val seqString = jsonReader.readJsonName() // check that it's "seq"
    jsonReader.skipAfter(JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[Any]()
    for (i <- 1 to size) {
      val element = parentDeserializer.deserialize(elementType, jsonReader)
      res += element
      jsonReader.skipAfter(JsonReader.p(","))
    }
    jsonReader.skipAfter(JsonReader.BRACKET_CLOSE)
    jsonReader.skipAfter(JsonReader.CURLY_CLOSE)
    // TODO: this is where we would change things to handle precise subclasses like List vs Vector
    res.toList
  }

  override def deserialize(t: universe.Type, src: JsonReader): Seq[_] = null // TODO: throw relevant exception
}
