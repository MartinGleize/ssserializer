package ssserializer.serializers.json.compact

import java.io.BufferedWriter

import ssserializer.serializers.json.JsonSerializer
import ssserializer.serializers.json.compact.traversal.{CompactJsonMemory, RefTraversalSerializer}
import ssserializer.serializers.json.compact.writing.{DependencyWritingSerializer, NullWritingSerializer}
import ssserializer.serializers.{MasterSerializer, Serializer}
import ssserializer.typing.Detector
import ssserializer.typing.detectors.anyDetector

import scala.reflect.runtime.universe._

class CompactJsonSerializer extends MasterSerializer[BufferedWriter] {

  private val refTraversal = new RefTraversalSerializer()

  override def serialize[T : TypeTag](data: T, dest: BufferedWriter): Unit = {
    // 1st pass: recurse on the object to store all the refs needed in the JSON array
    val memory = new CompactJsonMemory()
    refTraversal.serialize(data, typeOf[T], memory, refTraversal)
    // 2nd pass: write the objects in memory 1 by 1 in a Json array
    // TODO: make this a private field, remove the constructor dependency to the memory (and make the memory part of 'dest')
    val dependencyWritingSerializer = new DependencyWritingSerializer(memory)
    // dependencyWritingSerializer writes value types as usually encoded in JSON, and replaces ref types with their index in memory
    serializeMemory(memory, dest, dependencyWritingSerializer)
  }

  private def serializeMemory(memory: CompactJsonMemory, dest: BufferedWriter, dependencyWritingSerializer: MasterSerializer[BufferedWriter]): Unit = {
    // iterate through the memory and write all the JSON references
    dest.write("[")
    val refs = memory.refs
    for (i <- refs.indices) {
      val (ref, refType) = refs(i)
      serialize(ref, refType, dest, dependencyWritingSerializer)
      if (i != refs.size - 1) {
        dest.write(",")
      }
    }
    dest.write("]")
    dest.flush()
  }

  /** Pairs of a detector and the serializer supporting the type it detects. */
  override val serializers: Seq[(Detector, Serializer[BufferedWriter])] = {
    val usualJsonSerializers = new JsonSerializer().serializers
    // we need this because we have an untyped (Any) null pointer this time, common to all types
    val nullFailsafe = anyDetector -> new NullWritingSerializer()
    usualJsonSerializers :+ nullFailsafe
  }
}
