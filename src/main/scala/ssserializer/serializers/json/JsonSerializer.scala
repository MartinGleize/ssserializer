package ssserializer.serializers.json

import java.io.BufferedWriter

import ssserializer.serializers.MasterSerializer
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

import scala.collection.immutable.ArraySeq

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
    seqDetector -> new SeqSerializer[Seq[_]](),
    mapDetector -> new MapSerializer(),
    tupleDetector -> new TupleSerializer(),
    caseClassDetector -> new CaseClassSerializer()
  )

}
