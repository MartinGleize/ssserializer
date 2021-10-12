package ssserializer.serializers.generic

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

trait MapSerializer[Output] extends NullHandlingSerializer[Map[_, _], Output] {

  /** Happens at the very start, before any (key,value) pair is serialized */
  def outputStart(output: Output): Unit

  /** Happens at the very end, after all (key,value) pairs have been serialized (also if the map was empty) */
  def outputEnd(output: Output): Unit

  /** Happens before each (key,value) pair is serialized */
  def outputBeforeKey(output: Output, isFirstEntry: Boolean): Unit

  /** Happens before each value of a (key,value) pair is serialized (after its key has been serialized) */
  def outputBeforeValue(output: Output): Unit

  /** Happens after the value of a (key,value) pair is serialized */
  def outputAfterValue(output: Output): Unit

  override def serializeNonNull(map: Map[_, _], t: Type, output: Output, parentSerializer: MasterSerializer[Output]): Unit = {
    outputStart(output)
    if (map.nonEmpty) {
      val keyType = t.typeArgs(0)
      val valueType = t.typeArgs(1)
      val it = map.iterator
      // write the first element
      assert(it.hasNext)
      val (key, value) = it.next()
      serializeKeyValuePair(key, keyType, value, valueType, isFirstEntry = true, output, parentSerializer)
      // write the tail
      while (it.hasNext) {
        val (key, value) = it.next()
        serializeKeyValuePair(key, keyType, value, valueType, isFirstEntry = false, output, parentSerializer)
      }
    }
    outputEnd(output)
  }

  def serializeKeyValuePair(key: Any, keyType: Type, value: Any, valueType: Type, isFirstEntry: Boolean,
                            output: Output, parentSerializer: MasterSerializer[Output]): Unit = {
    outputBeforeKey(output, isFirstEntry)
    parentSerializer.serialize(key, keyType, output)
    outputBeforeValue(output)
    parentSerializer.serialize(value, valueType, output)
    outputAfterValue(output)
  }
}