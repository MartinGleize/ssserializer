package ssserializer

import java.io.{InputStream, OutputStream}

import ssserializer.deserializers.Deserializer
import ssserializer.serializers.Serializer

/**
 * Provides the implicit JSON serializer and deserializer used by the main library calls.
 * Everything should be accessible by using these 2 imports:
 * import ssserializer._
 * import ssserializer.json._
 *
 */
package object json {

  implicit val serializer: Serializer[OutputStream] = new JsonSerializer()

  implicit val deserializer: Deserializer[Any, InputStream] = new JsonDeserializer()
}
