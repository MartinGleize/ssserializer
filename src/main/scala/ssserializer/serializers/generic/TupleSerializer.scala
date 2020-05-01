package ssserializer.serializers.generic

import scala.reflect.runtime.universe._

/**
 * A generic serializer for tuples of all arities (written as sequences of their arguments).
 */
trait TupleSerializer[Output] extends IteratorSerializer[Product, Output] {

  /** Returns an iterator of elements contained in the Product/Tuple */
  override def iterator(data: Product): Iterator[_] = data.productIterator

  /** Returns the types of each element in the Product, in order of declaration */
  override def typeIterator(t: Type): Iterator[Type] = t.typeArgs.iterator

}
