package ssserializer.serializers.json

import ssserializer.serializers.{MasterSerializer, Serializer}
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

class JsonSerializer extends MasterSerializer {

  override val serializers: Seq[(Detector, Serializer)] = Seq(
    doubleDetector -> new ToStringSerializer(),
    longDetector -> new ToStringSerializer(),
    intDetector -> new ToStringSerializer(),
    booleanDetector -> new ToStringSerializer(),
    stringDetector -> new StringSerializer(),
    seqDetector -> new SeqSerializer(),
    mapDetector -> new MapSerializer(),
    caseClassDetector -> new CaseClassSerializer()
  )

}
