package ssserializer.serializers.generic

import ssserializer.serializers.MasterSerializer
import scala.reflect.runtime.universe._

trait IteratorSerializer[S, Output] extends NullHandlingSerializer[S, Output] {

  /** Returns an iterator of elements contained in the supposed sequence-like 'data' */
  def iterator(data: S): Iterator[_]

  /** Happens at the very start of the iteration, before any element is serialized */
  def outputStart(output: Output): Unit

  /** Happens at the very end of the iteration, after all elements have been serialized (happens even when empty) */
  def outputEnd(output: Output): Unit

  /** Happens after an element has been serialized in the iteration */
  def outputAfterElement(element: Any, elementType: Type, output: Output, hasNext: Boolean): Unit

  /** Returns a companion iterator of the types of each element in the sequence-like data
   * In practice for most sequences, it will continually return the one type parameter,
   * but for Product/Tuple it's convenient to produce each of the types in the same way we do elements.
   * */
  def typeIterator(t: Type): Iterator[Type] = Iterator.continually(t.typeArgs.head)

  override def serializeNonNull(seq: S, t: Type, output: Output, parentSerializer: MasterSerializer[Output]): Unit = {
    val it = iterator(seq)
    val typeIt = typeIterator(t)
    outputStart(output)
    while (it.hasNext) {
      val element = it.next()
      // at this point, typeIterator.hasNext should be true too
      val elementType = typeIt.next()
      parentSerializer.serialize(element, elementType, output)
      outputAfterElement(element, elementType, output, it.hasNext)
    }
    outputEnd(output)
  }

}
