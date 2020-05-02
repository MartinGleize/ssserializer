package ssserializer.serializers.json.compact.traversal
import scala.reflect.runtime.universe

class CaseClassTraversalSerializer extends ssserializer.serializers.generic.CaseClassSerializer[CompactJsonMemory]
  with RecursiveRefTraversalSerializer[Product] {

  override def outputStart(output: CompactJsonMemory): Unit = ()
  override def outputEnd(output: CompactJsonMemory): Unit = ()
  override def outputBeforeProductArg(argName: String, argType: universe.Type, output: CompactJsonMemory): Unit = ()
  override def outputAfterProductArg(arg: Any, argType: universe.Type, output: CompactJsonMemory, hasNextProductArg: Boolean): Unit = ()
}
