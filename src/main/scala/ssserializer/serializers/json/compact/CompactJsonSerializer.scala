package ssserializer.serializers.json.compact

import java.io.BufferedWriter

import ssserializer.serializers.json.compact.traversal.{CompactJsonMemory, RefTraversalSerializer}
import ssserializer.serializers.{MasterSerializer, Serializer}
import ssserializer.typing.Detector

import scala.reflect.runtime.universe._

class CompactJsonSerializer extends MasterSerializer[BufferedWriter] {

  override def serialize[T : TypeTag](data: T, dest: BufferedWriter): Unit = {
    // 1st pass: recurse on the object to store all the refs needed in the JSON array
    val refTraversal = new RefTraversalSerializer()
    val memory = new CompactJsonMemory()
    refTraversal.serialize(data, typeOf[T], memory, refTraversal)
  }

  /** Pairs of a detector and the serializer supporting the type it detects. */
  override def serializers: Seq[(Detector, Serializer[BufferedWriter])] = null
}
