package ssserializer.serializers.json.compact.traversal

import ssserializer.serializers.MasterSerializer
import ssserializer.serializers.json.compact.writing.NullWritingSerializer
import ssserializer.typing.Detector
import ssserializer.typing.detectors.{anyDetector, _}

import scala.reflect.runtime.universe

class RefTraversalSerializer extends MasterSerializer[CompactJsonMemory] {

  override val serializers: Seq[(Detector, ssserializer.serializers.Serializer[CompactJsonMemory])] = Seq(
    valDetector -> new IgnoreTraversalSerializer(),
    stringDetector -> new PrimitiveRefTraversalSerializer(),
    optionDetector -> new OptionTraversalSerializer(),
    arrayDetector -> new IteratorTraversalSerializer[Array[_]]() {
      /** Returns an iterator of elements contained in the supposed sequence-like 'data' */
      override def iterator(data: Array[_]): Iterator[_] = data.iterator // there is an implicit conversion here, to ArrayOps
    },
    seqDetector[Seq[_]] -> new SeqTraversalSerializer[Seq[_]](),
    setDetector -> new SeqTraversalSerializer[Set[_]],
    mapDetector -> new MapTraversalSerializer(),
    tupleDetector -> new TupleTraversalSerializer(),
    caseClassDetector -> new CaseClassTraversalSerializer()
    /*
    anyDetector -> new NullHandlingTraversalSerializer[Any] {
      override def serializeNonNull(data: Any, t: universe.Type, output: CompactJsonMemory, parentSerializer: MasterSerializer[CompactJsonMemory]): Unit = {
        // this means 'data' wasn't actually null so the serializer couldn't handle the type
        throw new RuntimeException("Writing of type not handled: " + t)
      }
    }
    */
  )

  class SeqTraversalSerializer[S <: Iterable[_]] extends ssserializer.serializers.generic.SeqSerializer[S, CompactJsonMemory]
    with IteratorTraversalSerializer[S]

  class TupleTraversalSerializer extends ssserializer.serializers.generic.TupleSerializer[CompactJsonMemory]
    with IteratorTraversalSerializer[Product]
}
