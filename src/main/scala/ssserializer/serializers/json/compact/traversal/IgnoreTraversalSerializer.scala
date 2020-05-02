package ssserializer.serializers.json.compact.traversal

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe

/**
 * Ignores its input and does nothing
 */
class IgnoreTraversalSerializer extends NullHandlingTraversalSerializer[Any] {

  override def outputNull(output: CompactJsonMemory): Unit = {
    // this should never happen, so we throw an exception here as a sanity check
    throw new AssertionError(this.getClass + " has encountered 'null', but should never be used to traverse an AnyRef")
  }

  override def serializeNonNull(data: Any, t: universe.Type, output: CompactJsonMemory, parentSerializer: MasterSerializer[CompactJsonMemory]): Unit = {
    // do nothing
  }
}
