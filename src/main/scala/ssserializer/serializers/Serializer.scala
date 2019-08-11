package ssserializer.serializers

import java.io.OutputStream
import scala.reflect.runtime.universe._

/**
 * A serializer.
 * @tparam Output the destination for the serialized object (often a writer or an output stream)
 */
trait Serializer[Output] {

  def serialize(data: Any, t: Type, dest: Output, parentSerializer: MasterSerializer[Output] = null): Unit

  final def serialize[T : TypeTag](data: T, dest: Output): Unit = {
    // in effect, this is type erasure
    serialize(data, typeOf[T], dest)
  }
}
