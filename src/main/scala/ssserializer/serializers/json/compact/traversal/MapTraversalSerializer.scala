package ssserializer.serializers.json.compact.traversal

class MapTraversalSerializer extends ssserializer.serializers.generic.MapSerializer[CompactJsonMemory]
  with RecursiveRefTraversalSerializer[Map[_, _]] {

  override def outputStart(output: CompactJsonMemory): Unit = ()
  override def outputEnd(output: CompactJsonMemory): Unit = ()
  override def outputBeforeKey(output: CompactJsonMemory, isFirstEntry: Boolean): Unit = ()
  override def outputBeforeValue(output: CompactJsonMemory): Unit = ()
  override def outputAfterValue(output: CompactJsonMemory): Unit = ()
}
