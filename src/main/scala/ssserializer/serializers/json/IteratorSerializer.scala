package ssserializer.serializers.json

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

trait IteratorSerializer[S] extends Serializer[S] {

  /** Returns an iterator of elements contained in the supposed sequence-like 'data' */
  def iterator(data: S): Iterator[_]

  /** Returns a companion iterator of the types of each element in the sequence-like data
   * In practice for most sequences, it will continually return the one type parameter,
   * but for Product/Tuple it's convenient to produce each of the types in the same way we do elements.
   * */
  def typeIterator(t: Type): Iterator[Type] = Iterator.continually(t.typeArgs.head)

  override def serializeNonNull(seq: S, t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    val it = iterator(seq)
    val typeIt = typeIterator(t)
    w.write("[")
    while (it.hasNext) {
      val element = it.next()
      // at this point, typeIterator.hasNext should be true too
      val elementType = typeIt.next()
      parentSerializer.serialize(element, elementType, w)
      // if this wasn't the last element, add the separating comma
      if (it.hasNext) {
        w.write(",")
      }
    }
    w.write("]")
  }

}
