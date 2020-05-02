package ssserializer.serializers.json.compact.traversal
import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe

/**
 * Traverses references that do not depend on any other reference, like Strings.
 */
class PrimitiveRefTraversalSerializer[T <: AnyRef] extends NullHandlingTraversalSerializer[T] {
  override def serializeNonNull(data: T, t: universe.Type,
                                output: CompactJsonMemory,
                                parentSerializer: MasterSerializer[CompactJsonMemory]): Unit = {
    // just register the ref and nothing else
    // Essentially used for "String" right now (but could be used for things like BigInt)
    output.registerRef(data, t)
  }
}
