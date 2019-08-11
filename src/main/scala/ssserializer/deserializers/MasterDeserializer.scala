package ssserializer.deserializers

import ssserializer.typing._

import scala.reflect.runtime.universe._

/**
 * A deserializer that delegates its task to specialized deserializers for their detected type
 *
 * @tparam Input the serialized input (often a reader or a stream)
 */
trait MasterDeserializer[Input] extends Deserializer[Any, Input] {

  /** Pairs of a detector and the deserializer supporting the type it detects */
  def deserializers: Seq[(Detector, Deserializer[_, Input])]

  override def deserialize(t: Type, src: Input, parentDeserializer: MasterDeserializer[Input] = null): Any = {
    // TODO: throw some custom exception for non-handled type
    // TODO: throw some internal dev-only exception for parentDeserializer not being null on this call
    val deserializer = TypeMapper.map(t, deserializers).getOrElse(throw new RuntimeException("Type not handled"))
    deserializer.deserialize(t, src, this)
  }

}
