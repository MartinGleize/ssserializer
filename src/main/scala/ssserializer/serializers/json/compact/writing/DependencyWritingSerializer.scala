package ssserializer.serializers.json.compact.writing

import java.io.BufferedWriter

import ssserializer.serializers.MasterSerializer
import ssserializer.serializers.json.ToStringSerializer
import ssserializer.serializers.json.compact.traversal.CompactJsonMemory
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

class DependencyWritingSerializer(val memory: CompactJsonMemory) extends MasterSerializer[BufferedWriter] {
  /** Pairs of a detector and the serializer supporting the type it detects. */
  override val serializers: Seq[(Detector, ssserializer.serializers.Serializer[BufferedWriter])] = Seq(
    doubleDetector -> new ToStringSerializer(),
    longDetector -> new ToStringSerializer(),
    intDetector -> new ToStringSerializer(),
    booleanDetector -> new ToStringSerializer(),
    anyDetector -> new RefWritingSerializer()
  )
}
