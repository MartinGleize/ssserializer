package ssserializer.serializers.json

import java.io.BufferedWriter

class MapSerializer extends ssserializer.serializers.generic.MapSerializer[BufferedWriter] with NullHandlingSerializer[Map[_, _]] {

  /** Happens at the very start, before any (key,value) pair is serialized */
  override def outputStart(w: BufferedWriter): Unit = {
    w.write("[")
  }

  /** Happens at the very end, after all (key,value) pairs have been serialized (also if the map was empty) */
  override def outputEnd(w: BufferedWriter): Unit = {
    w.write("]")
  }

  /** Happens before each (key,value) pair is serialized */
  override def outputBeforeKey(w: BufferedWriter): Unit = {
    w.write("{\"k\":")
  }

  /** Happens before each value of a (key,value) pair is serialized (after its key has been serialized) */
  override def outputBeforeValue(w: BufferedWriter): Unit = {
    w.write(",\"v\":")
  }

  /** Happens after the value of a (key,value) pair is serialized */
  override def outputAfterValue(w: BufferedWriter, hasNextEntry: Boolean): Unit = {
    w.write("}")
    if (hasNextEntry) {
      w.write(",")
    }
  }

}
