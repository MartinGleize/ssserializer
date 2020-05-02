package ssserializer.serializers.json.compact.traversal

import ssserializer.serializers.MasterSerializer
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

class RefTraversalSerializer extends MasterSerializer[CompactJsonMemory] {

  override val serializers: Seq[(Detector, ssserializer.serializers.Serializer[CompactJsonMemory])] = Seq(
    Detector.ofExactBaseType(typeOf[AnyVal]) -> new IgnoreTraversalSerializer(),
    stringDetector -> new PrimitiveRefTraversalSerializer(),
    optionDetector -> new OptionTraversalSerializer(),
    arrayDetector -> new IteratorTraversalSerializer[Array[_]]() {
      /** Returns an iterator of elements contained in the supposed sequence-like 'data' */
      override def iterator(data: Array[_]): Iterator[_] = data.iterator // there is an implicit conversion here, to ArrayOps
    },
    seqDetector[Seq[_]] -> new SeqTraversalSerializer[Seq[_]](),
    setDetector -> new SeqTraversalSerializer[Set[_]],
    mapDetector -> new MapTraversalSerializer(),
    tupleDetector -> null, //new TupleTraversalSerializer(),
    caseClassDetector -> new CaseClassTraversalSerializer()
  )

  class SeqTraversalSerializer[S <: Iterable[_]] extends ssserializer.serializers.generic.SeqSerializer[S, CompactJsonMemory]
    with IteratorTraversalSerializer[S]

  /*
  class TupleTraversalSerializer extends ssserializer.serializers.generic.TupleSerializer[CompactJsonMemory]
    with IteratorTraversalSerializer[Product]
  */
}
