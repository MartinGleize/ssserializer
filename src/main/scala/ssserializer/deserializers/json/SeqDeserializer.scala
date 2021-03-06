package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

trait SeqDeserializer[S] extends Deserializer[S] {

  //TODO: might be able to use Factory or BuildFrom type of implicits here to ease the implementation */
  def constructFinalObject(elements: mutable.Seq[Any]): S

  /** Returns a companion iterator of the types of each element in the sequence-like data
   * In practice for most sequences, it will continually return the one type parameter,
   * but for Product/Tuple it's convenient to produce each of the types in the same way we do elements.
   * */
  def typeIterator(t: Type): Iterator[Type] = Iterator.continually(t.typeArgs.head)

  override def deserializeNonNull(t: Type, jsonReader: parsing.JsonReader, parentDeserializer: MasterDeserializer[parsing.JsonReader]): S = {
    val typeIt = typeIterator(t)
    jsonReader.skipAfter(parsing.JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[Any]()
    var potentialElement: Option[Any] = null
    while (typeIt.hasNext && {potentialElement = deserializeNextElement(typeIt.next(), jsonReader, parentDeserializer); potentialElement.isDefined}) {
      val element = potentialElement.get
      res += element
    }
    // TODO: this is where we would change things to construct objects of precise subclasses like List vs Vector
    constructFinalObject(res, t)
  }

  def deserializeNextElement(elementType: Type, jsonReader: parsing.JsonReader, parentDeserializer: MasterDeserializer[parsing.JsonReader]): Option[Any] = {
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
  }

  def constructFinalObject(elements: mutable.Seq[Any], t: Type): S = constructFinalObject(elements)
}
