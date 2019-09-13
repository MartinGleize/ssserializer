package ssserializer.deserializers

import scala.reflect.runtime.universe._

class DeserializationException(message: String, tpe: Type) extends Exception(message) {

  override def getMessage: String = {
    "Error trying to deserialize type " + tpe + ":\n" + message
  }
}
