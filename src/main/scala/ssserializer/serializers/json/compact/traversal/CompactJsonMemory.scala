package ssserializer.serializers.json.compact.traversal

import java.util

import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

/**
 * Represents the state of the first pass in the CompactJsonSerializer, used as type 'Output'
 * Not thread-safe.
 */
class CompactJsonMemory {

  // the ref tree traversed in a depth-first way (refs are not repeated if they were seen earlier in the tree)
  private val refList = new ArrayBuffer[(Any, Type)]()
  // positions of each ref in the previous list
  private val refPositions = new util.IdentityHashMap[Any, Int]()

  def registerRef(ref: Any, refType: Type): Unit = {
    if (!refPositions.containsKey(ref)) {
      val index = refList.size
      refList += ((ref, refType))
      refPositions.put(ref, index)
    }
  }

  def refs: Seq[(Any, Type)] = refList.toList

}
