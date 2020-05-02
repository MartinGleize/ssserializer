package ssserializer.serializers.json.compact.traversal

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe

trait RecursiveRefTraversalSerializer[T] extends NullHandlingTraversalSerializer[T] {

  abstract override def serializeNonNull(data: T, t: universe.Type,
                                output: CompactJsonMemory,
                                parentSerializer: MasterSerializer[CompactJsonMemory]): Unit = {
    // always register the parent first, then the children
    output.registerRef(data, t)
    // traverse the children as usual here
    super.serializeNonNull(data, t, output, parentSerializer)
  }
}
