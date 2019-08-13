package ssserializer.serializers.json

class SeqSerializer[S <: IterableOnce[_]] extends IteratorSerializer[S] {
  /** Returns an iterator of elements contained in the sequence */
  override def iterator(data: S): Iterator[_] = data.iterator
}
