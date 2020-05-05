package ssserializer.deserializers.json.compact

import ssserializer.deserializers.json.compact.toplevel.TopLevelArrayDeserializer
import ssserializer.deserializers.{Deserializer, MasterDeserializer}
import ssserializer.deserializers.json.parsing.JsonReader
import ssserializer.typing.Detector

import scala.reflect.runtime.universe._

class CompactJsonDeserializer extends MasterDeserializer[JsonReader] {

  private val topLevelArrayDeserializer = new TopLevelArrayDeserializer()

  override def deserialize(t: Type, src: JsonReader, parentDeserializer: MasterDeserializer[JsonReader] = this): Any = {
    // create a fresh reader with memory (also aware of the top-level deserializer)
    val jsonReaderWithMemory = new JsonReaderWithMemory(src, topLevelArrayDeserializer)
    // deserialize the object, starting with the parsing of the JSON array (first item is the object we want)
    topLevelArrayDeserializer.deserialize(t, jsonReaderWithMemory)
  }

  /** CompactJsonDeserializer only initializes the memory */
  override def deserializers: Seq[(Detector, Deserializer[_, JsonReader])] = Seq()
}
