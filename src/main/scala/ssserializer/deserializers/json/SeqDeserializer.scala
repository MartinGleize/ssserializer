package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

trait SeqDeserializer[T <: Seq[_]] extends Deserializer[T] {

  def constructFinalSequence(res: mutable.Seq[_]): T

  override def deserializeNonNull(t: Type, jsonReader: JsonReader, parentDeserializer: MasterDeserializer[JsonReader]): T = {
    val elementType = t.typeArgs.head
    jsonReader.skipAfter(JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[Any]()
    var potentialElement: Option[Any] = null
    while ({potentialElement = deserializeNextElement(elementType, jsonReader, parentDeserializer); potentialElement.isDefined}) {
      val element = potentialElement.get
      res += element
    }
    // TODO: this is where we would change things to construct objects of precise subclasses like List vs Vector
    constructFinalSequence(res)
  }

  def deserializeNextElement(elementType: Type, jsonReader: JsonReader, parentDeserializer: MasterDeserializer[JsonReader]): Option[Any] = {
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
