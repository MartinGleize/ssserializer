package ssserializer.serializers.json

import java.io.BufferedWriter

import ssserializer.serializers.MasterSerializer
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

class JsonSerializer extends MasterSerializer[BufferedWriter] {

  override val serializers: Seq[(Detector, Serializer[_])] = Seq(
    doubleDetector -> new ToStringSerializer(),
    longDetector -> new ToStringSerializer(),
    intDetector -> new ToStringSerializer(),
    booleanDetector -> new ToStringSerializer(),
    stringDetector -> new StringSerializer(),
    optionDetector -> new OptionSerializer(),
    seqDetector -> new SeqSerializer(),
    mapDetector -> new MapSerializer(),
    caseClassDetector -> new CaseClassSerializer()
  )

}
