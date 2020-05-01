package ssserializer.serializers.generic

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

trait MapSerializer[Output] extends NullHandlingSerializer[Map[_, _], Output] {

  /** Happens at the very start, before any (key,value) pair is serialized */
  def outputStart(output: Output): Unit

  /** Happens at the very end, after all (key,value) pairs have been serialized (also if the map was empty) */
  def outputEnd(output: Output): Unit

  /** Happens before each (key,value) pair is serialized */
  def outputBeforeKey(output: Output): Unit

  /** Happens before each value of a (key,value) pair is serialized (after its key has been serialized) */
  def outputBeforeValue(output: Output): Unit

  /** Happens after the value of a (key,value) pair is serialized */
  def outputAfterValue(output: Output, hasNextEntry: Boolean): Unit

  override def serializeNonNull(map: Map[_, _], t: Type, output: Output, parentSerializer: MasterSerializer[Output]): Unit = {
    outputStart(output)
    val keyType = t.typeArgs(0)
    val valueType = t.typeArgs(1)
    val size = map.size
    for (((key, value), index) <- map.zipWithIndex) {
      outputBeforeKey(output)
      parentSerializer.serialize(key, keyType, output)
      outputBeforeValue(output)
      parentSerializer.serialize(value, valueType, output)
      outputAfterValue(output, index != size - 1)
    }
    outputEnd(output)
  }

}