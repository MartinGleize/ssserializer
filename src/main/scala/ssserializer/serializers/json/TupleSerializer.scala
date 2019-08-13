package ssserializer.serializers.json

import scala.reflect.runtime.universe._

/**
 * A JSON serializer for tuples of all arities (written as JSON arrays).
 */
class TupleSerializer extends IteratorSerializer[Product] {

  /** Returns an iterator of elements contained in the Product/Tuple */
  override def iterator(data: Product): Iterator[_] = data.productIterator

  /** Returns the types of each element in the Product, in order of declaration */
  override def typeIterator(t: Type): Iterator[Type] = t.typeArgs.iterator

}
