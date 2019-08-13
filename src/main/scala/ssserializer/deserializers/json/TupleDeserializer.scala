package ssserializer.deserializers.json

import scala.collection.mutable
import scala.reflect.runtime.universe

/**
 * A JSON deserializer for tuples of all arities. The dynamic product construction from CaseClassDeserialization is re-used,
 * combined with a mixin to the JSON array reading of SeqDeserializer.
 */
class TupleDeserializer extends CaseClassDeserializer with SeqDeserializer[Product] {

  override def typeIterator(t: universe.Type): Iterator[universe.Type] = t.typeArgs.iterator

  override def constructFinalObject(elements: mutable.Seq[_], t: universe.Type): Product = newProduct(t, elements.toSeq)

  override def constructFinalObject(elements: mutable.Seq[_]): Product = throw new RuntimeException("Should not be called")
}
