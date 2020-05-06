package ssserializer.json.compact

import java.io.{InputStream, InputStreamReader}
import java.nio.charset.StandardCharsets

import ssserializer.deserializers.json.compact.CompactJsonDeserializer
import ssserializer.deserializers.{Deserializer, MasterDeserializer}
import ssserializer.deserializers.json.parsing.{JsonReader, ReallyFastJsonReader}

import scala.reflect.runtime.universe._

class JsonDeserializer extends Deserializer[Any, InputStream] {

  val deserializer: MasterDeserializer[JsonReader] = new CompactJsonDeserializer()

  override def deserialize(t: Type, src: InputStream, parentDeserializer: MasterDeserializer[InputStream]): Any = {
    val jsonReader = new ReallyFastJsonReader(new InputStreamReader(src, StandardCharsets.UTF_8))
    val res = deserializer.deserialize(t, jsonReader)
    jsonReader.close()
    res
  }

}
