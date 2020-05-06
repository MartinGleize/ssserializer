package ssserializer.serializers.json.compact.writing

import java.io.BufferedWriter

import ssserializer.serializers.{MasterSerializer, Serializer}
import ssserializer.serializers.json.JsonSerializer
import ssserializer.typing.Detector
import ssserializer.typing.detectors.{anyDetector, caseClassDetector}

class TopLevelWritingSerializer extends MasterSerializer[BufferedWriter] {

  /** Pairs of a detector and the serializer supporting the type it detects. */
  override val serializers: Seq[(Detector, Serializer[BufferedWriter])] = {
    val usualJsonSerializers = new JsonSerializer().serializers
      .filter { case (detector, _) => detector != caseClassDetector }
    // we replace the CaseClassSerializer with a more character-efficient version
    val caseClassSerializer = caseClassDetector -> new CompactCaseClassSerializer()
    // we need this because we have an untyped (Any) null pointer this time, common to all types
    val nullFailsafe = anyDetector -> new NullWritingSerializer()
    usualJsonSerializers :+ caseClassSerializer :+ nullFailsafe
  }
}
