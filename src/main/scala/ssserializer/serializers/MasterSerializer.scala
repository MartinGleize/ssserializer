package ssserializer.serializers

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
  /** Pairs of a detector and the serializer supporting the type it detects. */
  def serializers: Seq[(Detector, Serializer[Output])]

  /** Maps the types to the correct serializers. */
  val typeMapper = new TypeMapper[Serializer[Output]]

  override def serialize(data: Any, t: Type, dest: Output, parentSerializer: MasterSerializer[Output] = this): Unit = {
    // TODO: throw some custom exception for non-handled type
    val (serializer, dealiasedType) = typeMapper.map(t, serializers)
      .getOrElse(throw new RuntimeException("Type not handled: " + t))
    // parentSerializer will deal with the dependencies, by default it's the same MasterSerializer
    serializer.serialize(data, dealiasedType, dest, parentSerializer)
  }
}
