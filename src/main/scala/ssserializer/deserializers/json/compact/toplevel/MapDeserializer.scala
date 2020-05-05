package ssserializer.deserializers.json.compact.toplevel

import ssserializer.deserializers.json.compact.JsonReaderWithMemory

import scala.collection.mutable.ArrayBuffer

class MapDeserializer extends ssserializer.deserializers.json.MapDeserializer[JsonReaderWithMemory] {

  override def buildFinalMap(keyValuePairs: Seq[(Any, Any)], jsonReader: JsonReaderWithMemory): Map[Any, Any] = {
    // flatten the pairs
    val elements = keyValuePairs.flatMap(kv => Seq(kv._1, kv._2))
    // resolve ref dependencies
    val realElements = jsonReader.topLevelArrayDeserializer.deserializeRefDependencies(elements, jsonReader)
    // rebuild the pairs
    val realKeyValuePairs = new ArrayBuffer[(Any, Any)]
    var i = 0
    while (i + 1 < realElements.size) {
      val pair = (realElements(i), realElements(i + 1))
      realKeyValuePairs += pair
      i += 2
    }
    // build the final map
    super.buildFinalMap(realKeyValuePairs, jsonReader)
  }

}