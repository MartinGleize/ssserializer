package ssserializer.serializers

import java.io.OutputStream
import scala.reflect.runtime.universe._

/**
 * A serializer.
 */
trait Serializer {

  def serialize(data: Any, t: Type, dest: OutputStream, parentSerializer: MasterSerializer = null): Unit

  final def serialize[T : TypeTag](data: T, dest: OutputStream): Unit = {
    // in effect, this is type erasure
    serialize(data, typeOf[T], dest)
  }
}
