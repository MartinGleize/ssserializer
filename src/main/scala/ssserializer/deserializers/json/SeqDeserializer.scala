package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer
import ssserializer.deserializers.json.parsing.JsonReader

import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

trait SeqDeserializer[S, JsonInput <: JsonReader] extends NullHandlingDeserializer[S, JsonInput] {

  def buildFinalObject(elements: Seq[Any], t: Type, jsonInput: JsonInput): S

  /** Returns a companion iterator of the types of each element in the sequence-like data
   * In practice for most sequences, it will continually return the one type parameter,
   * but for Product/Tuple it's convenient to produce each of the types in the same way we do elements.
   * */
  def typeIterator(t: Type): Iterator[Type] = Iterator.continually(t.typeArgs.head)

  override def deserializeNonNull(t: Type, jsonReader: JsonInput, parentDeserializer: MasterDeserializer[JsonInput]): S = {
    val typeIt = typeIterator(t)
    jsonReader.skipAfter(parsing.JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[Any]()
    var potentialElement: Option[Any] = null
    while ({potentialElement = deserializeNextElement(typeIt, jsonReader, parentDeserializer); potentialElement.isDefined}) {
      val element = potentialElement.get
      res += element
    }
    // this is where we would change things to construct objects of precise subclasses like List vs Vector
    buildFinalObject(res, t, jsonReader)
  }

  def deserializeNextElement(typeIterator: Iterator[Type], jsonReader: JsonInput, parentDeserializer: MasterDeserializer[JsonInput]): Option[Any] = {
    // check that we still have valid types to associate to the elements
    if (typeIterator.hasNext) {
      val elementType = typeIterator.next()
      // first try to look for the end of the JSON array
      if (jsonReader.tryToConsumeToken(parsing.JsonReader.BRACKET_CLOSE)) {
        None
      } else {
        // can't read a "]" so a new element can be read
        val element = parentDeserializer.deserialize(elementType, jsonReader)
        // try to read a "," after (if it fails it likely means this would be the last element)
        jsonReader.tryToConsumeToken(parsing.JsonReader.COMMA)
        Some(element)
      }
    } else {
      // we didn't have any valid type left, we still have to try to read the end character to close the sequence properly
      jsonReader.tryToConsumeToken(parsing.JsonReader.BRACKET_CLOSE)
      None
    }
  }

}
