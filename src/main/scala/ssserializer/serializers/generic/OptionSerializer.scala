package ssserializer.serializers.generic
import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

trait OptionSerializer[Output] extends NullHandlingSerializer[Option[_], Output] {

  /** Happens before the option is serialized */
  def outputStart(output: Output): Unit

  /** Happens after the value of the option has been serialized */
  def outputEnd(option: Option[_], output: Output): Unit

  override def serializeNonNull(data: Option[_], t: Type, output: Output, parentSerializer: MasterSerializer[Output]): Unit = {
    val elementType = t.typeArgs.head
    outputStart(output)
    if (data.isDefined) {
      parentSerializer.serialize(data.get, elementType, output)
    }
    outputEnd(data, output)
  }

}
