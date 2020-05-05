package ssserializer.deserializers.json.compact

import ssserializer.deserializers.json.{NumberStringDeserializer, defaults}
import ssserializer.deserializers.{Deserializer, MasterDeserializer}
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

import scala.reflect.runtime.universe

/** Reads only the dependencies of top-level JSON array references.
 * Dependencies are either encoded:
 * - normally for value types (their canonical JSON representation)
 * - with a Integer for reference types (including null) */
class RefParsingDeserializer extends MasterDeserializer[JsonReaderWithMemory] {

  /** Pairs of a detector and the deserializer supporting the type it detects */
  override def deserializers: Seq[(Detector, Deserializer[_, JsonReaderWithMemory])] = Seq(
    doubleDetector -> defaults.doubleDeserializer,
    longDetector -> defaults.longDeserializer,
    intDetector -> defaults.intDeserializer,
    booleanDetector -> defaults.booleanDeserializer,
    // ref-type dependencies are deserialized as simple integers
    anyDetector -> new NumberStringDeserializer[IntAsRef, JsonReaderWithMemory]() {
      override def parse(stringResult: String, t: universe.Type): IntAsRef = {
        IntAsRef(stringResult.toInt, t)
      }
      override def parse(stringResult: String): IntAsRef = {
        throw new AssertionError("Unknown type, this call should never happen for the IntAsRef deserializer")
      }
    }
  )

}
