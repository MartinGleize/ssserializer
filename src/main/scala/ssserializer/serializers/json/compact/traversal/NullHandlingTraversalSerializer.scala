package ssserializer.serializers.json.compact.traversal

trait NullHandlingTraversalSerializer[T] extends ssserializer.serializers.generic.NullHandlingSerializer[T, CompactJsonMemory] {

  override def outputNull(output: CompactJsonMemory): Unit = {
    // handle null as a regular value (except we don't need its type to serialize it)
    output.registerRef(null, null)
  }
}
