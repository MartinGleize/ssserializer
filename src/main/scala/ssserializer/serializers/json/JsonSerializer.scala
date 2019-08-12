package ssserializer.serializers.json

import java.io.BufferedWriter

import ssserializer.serializers.{MasterSerializer, Serializers}
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

class JsonSerializer extends MasterSerializer[BufferedWriter] {

  override val serializers: Seq[(Detector, ssserializer.serializers.Serializer[BufferedWriter])] = Seq(
    doubleDetector -> new ToStringSerializer(),
    longDetector -> new ToStringSerializer(),
    intDetector -> new ToStringSerializer(),
    booleanDetector -> new ToStringSerializer(),
    stringDetector -> new StringSerializer(),
    optionDetector -> new OptionSerializer(),
    arrayDetector -> new SeqSerializer[Seq[_]]().convertToSerializer[Array[_], Seq[_]](_.toList),
    seqDetector -> new SeqSerializer[Seq[_]](),
    mapDetector -> new MapSerializer(),
    caseClassDetector -> new CaseClassSerializer()
  )

}
