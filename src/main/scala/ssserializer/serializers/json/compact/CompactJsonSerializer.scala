package ssserializer.serializers.json.compact

import java.io.BufferedWriter

import ssserializer.serializers.json.JsonSerializer
import ssserializer.serializers.json.compact.traversal.{CompactJsonMemory, RefTraversalSerializer}
import ssserializer.serializers.json.compact.writing.{CompactCaseClassSerializer, DependencyWritingSerializer, NullWritingSerializer, TopLevelWritingSerializer}
import ssserializer.serializers.{MasterSerializer, Serializer}
import ssserializer.typing.Detector
import ssserializer.typing.detectors.{anyDetector, caseClassDetector}

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

/**
 * A JSON serializer. Produces a memory-efficient representation (a single reference is serialized only once).
 * Proceeds in two passes:
 * First pass traverses the object to serialize for all the references it depends on.
 * Second pass writes the list of these references as a JSON array.
 */
class CompactJsonSerializer extends Serializer[BufferedWriter] {

  private val refTraversal = new RefTraversalSerializer()
  private val topLevelWriting = new TopLevelWritingSerializer()
  private val fallbackNoDependencySerializer = new JsonSerializer()

  override def serialize(data: Any, t: universe.Type, dest: BufferedWriter, parentSerializer: MasterSerializer[BufferedWriter]): Unit = {
    // 1st pass: recurse on the object to store all the refs needed in the JSON array
    val memory = new CompactJsonMemory()
    refTraversal.serialize(data, t, memory, refTraversal)
    // 2nd pass: write the objects in memory 1 by 1 in a Json array
    // TODO: make this a private field, remove the constructor dependency to the memory (and make the memory part of 'dest')
    val dependencyWritingSerializer = new DependencyWritingSerializer(memory)
    // dependencyWritingSerializer writes value types as usually encoded in JSON, and replaces ref types with their index in memory
    serializeMemory(data, t, memory, dest, dependencyWritingSerializer)
  }

  private def serializeMemory(data: Any, t: universe.Type, memory: CompactJsonMemory, dest: BufferedWriter, dependencyWritingSerializer: MasterSerializer[BufferedWriter]): Unit = {
    // iterate through the memory and write all the JSON references
    dest.write("[")
    val refs = memory.refs
    if (refs.size <= 1) {
      // it means the object to serialize itself had a value type or no ref dependencies
      // it is safe to write it as the usual JSON serializer would
      fallbackNoDependencySerializer.serialize(data, t, dest)
    } else {
      // it was a reference type
      for (i <- refs.indices) {
        val (ref, refType) = refs(i)
        topLevelWriting.serialize(ref, refType, dest, dependencyWritingSerializer)
        if (i != refs.size - 1) {
          dest.write(",")
        }
      }
    }
    dest.write("]")
    dest.flush()
  }
}
