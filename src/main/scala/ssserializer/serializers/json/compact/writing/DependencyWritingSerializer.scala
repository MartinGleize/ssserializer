package ssserializer.serializers.json.compact.writing

import java.io.BufferedWriter

import ssserializer.serializers.MasterSerializer
import ssserializer.serializers.json.ToStringSerializer
import ssserializer.serializers.json.compact.traversal.CompactJsonMemory
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

class DependencyWritingSerializer(val memory: CompactJsonMemory) extends MasterSerializer[BufferedWriter] {
  /** Writes the value types as normal, and the index in memory for the ref types */
  override val serializers: Seq[(Detector, ssserializer.serializers.Serializer[BufferedWriter])] = Seq(
    valDetector -> new ToStringSerializer(),
    anyDetector -> new RefWritingSerializer()
  )
}
