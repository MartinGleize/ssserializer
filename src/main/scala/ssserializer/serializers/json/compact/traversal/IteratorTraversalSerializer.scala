package ssserializer.serializers.json.compact.traversal

import scala.reflect.runtime.universe._

trait IteratorTraversalSerializer[S] extends ssserializer.serializers.generic.IteratorSerializer[S, CompactJsonMemory]
  with RecursiveRefTraversalSerializer[S] {

  override def outputStart(w: CompactJsonMemory): Unit = ()
  override def outputEnd(w: CompactJsonMemory): Unit = ()
  override def outputAfterElement(element: Any, elementType: Type, w: CompactJsonMemory, hasNext: Boolean): Unit = ()
}
