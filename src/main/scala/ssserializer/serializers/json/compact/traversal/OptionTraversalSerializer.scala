package ssserializer.serializers.json.compact.traversal

/**
 * Traverses option types and stores the references in the CompactJsonMemory instance.
 * Uses a mix-in to add the recursive memory-registration behavior on top of the generic option serialization
 */
class OptionTraversalSerializer extends ssserializer.serializers.generic.OptionSerializer[CompactJsonMemory]
  with RecursiveRefTraversalSerializer[Option[_]] {

  override def outputStart(output: CompactJsonMemory): Unit = ()
  override def outputEnd(option: Option[_], output: CompactJsonMemory): Unit = ()
}
