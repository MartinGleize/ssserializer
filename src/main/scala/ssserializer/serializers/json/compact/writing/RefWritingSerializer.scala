package ssserializer.serializers.json.compact.writing

import java.io.BufferedWriter

import ssserializer.serializers.{MasterSerializer, Serializer}

import scala.reflect.runtime.universe

/**
 * Writes AnyRef-type objects as their index in memory (as determined by the first traversal pass)
 */
class RefWritingSerializer extends Serializer[BufferedWriter] {

  override def serialize(data: Any, t: universe.Type, dest: BufferedWriter, parentSerializer: MasterSerializer[BufferedWriter]): Unit = {
    val master = parentSerializer.asInstanceOf[DependencyWritingSerializer]
    val index = master.memory.indexOf(data)
    if (index >= 0) {
      dest.write(String.valueOf(index))
    } else {
      // this ref has not been seen during traversal, which is definitely strange...
      // most likely this means an AnyVal type not (yet) handled by the master serializer (like "Short")
      throw new AssertionError("Ref not in memory, not seen during traversal: " + data.toString)
    }
  }

}
