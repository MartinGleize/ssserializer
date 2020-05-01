package ssserializer.serializers.generic

trait SeqSerializer[S <: Iterable[_], Output] extends IteratorSerializer[S, Output] {
  /** Returns an iterator of elements contained in the sequence */
  override def iterator(data: S): Iterator[_] = data.iterator
}
