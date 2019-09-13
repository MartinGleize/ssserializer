package ssserializer.deserializers

import scala.reflect.runtime.universe._

class DeserializationException(cause: Throwable, tpe: Type) extends Exception(cause) {

  override def getMessage: String = {
    "Error trying to deserialize type " + tpe + (if (cause != null) "base exception: " + cause.getMessage else "")
  }
}
