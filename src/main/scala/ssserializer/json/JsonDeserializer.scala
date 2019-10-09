package ssserializer.json

import java.io.{InputStream, InputStreamReader}
import java.nio.charset.StandardCharsets

import ssserializer.deserializers.json.parsing.{FastJsonReader, JsonReader, ScannerJsonReader}
import ssserializer.deserializers.{Deserializer, MasterDeserializer}

import scala.reflect.runtime.universe._

class JsonDeserializer extends Deserializer[Any, InputStream] {

  val deserializer: MasterDeserializer[JsonReader] = new ssserializer.deserializers.json.JsonDeserializer()

  override def deserialize(t: Type, src: InputStream, parentDeserializer: MasterDeserializer[InputStream]): Any = {
    val jsonReader = new FastJsonReader(new InputStreamReader(src, StandardCharsets.UTF_8))
    val res = deserializer.deserialize(t, jsonReader)
    jsonReader.close()
    res
  }

}
