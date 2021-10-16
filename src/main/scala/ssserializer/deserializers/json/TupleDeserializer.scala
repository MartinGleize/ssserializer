package ssserializer.deserializers.json

import ssserializer.deserializers.json.parsing.JsonReader

import scala.reflect.runtime.universe

/**
 * A JSON deserializer for tuples of all arities. The dynamic product construction from CaseClassDeserialization is re-used,
 * combined with a mixin to the JSON array reading of SeqDeserializer.
 */
class TupleDeserializer[JsonInput <: JsonReader] extends CaseClassDeserializer[JsonInput] with SeqDeserializer[Product, JsonInput] {

  override def typeIterator(t: universe.Type): Iterator[universe.Type] = t.typeArgs.iterator

  override def buildFinalObject(elements: Seq[Any], t: universe.Type, jsonReader: JsonInput): Product = {
    newProduct(t, elements, jsonReader)
  }
}
