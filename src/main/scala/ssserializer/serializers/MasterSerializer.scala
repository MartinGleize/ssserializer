package ssserializer.serializers

import java.io.OutputStream

import ssserializer.deserializers.MasterDeserializer
import ssserializer.typing.TypeMapper
import ssserializer.typing._

import scala.reflect.runtime.universe._

/**
 * A serializer that delegates its task to specialized serializers for their detected type
 *
 */
trait MasterSerializer[Output] extends Serializer[Output] {

  /* TODO: pre-build some structure checking for Detector-Serializer compatibility, and also possibly ordering the checks
   * according to sub-types (like List must be detected before Seq) */
  /** Pairs of a detector and the serializer supporting the type it detects */
  def serializers: Seq[(Detector, Serializer[Output])]

  override def serialize(data: Any, t: Type, dest: Output, parentSerializer: MasterSerializer[Output] = null): Unit = {
    // TODO: throw some custom exception for non-handled type
    // TODO: throw some internal dev-only exception for parentDeserializer not being null on this call
    val serializer = TypeMapper.map(t, serializers).getOrElse(throw new RuntimeException("Type not handled"))
    serializer.serialize(data, t, dest, this)
  }
}
