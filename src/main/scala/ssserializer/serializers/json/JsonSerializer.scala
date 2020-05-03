package ssserializer.serializers.json

import java.io.BufferedWriter

import ssserializer.serializers.MasterSerializer
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

/**
 * A JSON serializer. Produces human-readable JSON.
 */
class JsonSerializer extends MasterSerializer[BufferedWriter] {

  override val serializers: Seq[(Detector, ssserializer.serializers.Serializer[BufferedWriter])] = Seq(
    doubleDetector -> new ToStringSerializer(),
    longDetector -> new ToStringSerializer(),
    intDetector -> new ToStringSerializer(),
    booleanDetector -> new ToStringSerializer(),
    stringDetector -> new StringSerializer(),
    optionDetector -> new OptionSerializer(),
    arrayDetector -> new IteratorSerializer[Array[_]]() {
      /** Returns an iterator of elements contained in the supposed sequence-like 'data' */
      override def iterator(data: Array[_]): Iterator[_] = data.iterator // there is an implicit conversion here, to ArrayOps
    },
    seqDetector[List[_]] -> new SeqSerializer[List[_]](),
    seqDetector[Vector[_]] -> new SeqSerializer[Vector[_]](),
    seqDetector[Seq[_]] -> new SeqSerializer[Seq[_]](),
    setDetector -> new SeqSerializer[Set[_]](),
    mapDetector -> new MapSerializer(),
    tupleDetector -> new TupleSerializer(),
    caseClassDetector -> new CaseClassSerializer()
  )

  /*########### Mixins for serializers with simple and composable implementations as iterator serializations ######## */

  class SeqSerializer[S <: Iterable[_]] extends ssserializer.serializers.generic.SeqSerializer[S, BufferedWriter]
    with IteratorSerializer[S]

  class TupleSerializer extends ssserializer.serializers.generic.TupleSerializer[BufferedWriter]
    with IteratorSerializer[Product]
}
