package ssserializer.deserializers.json.compact.toplevel

import ssserializer.deserializers.json.compact.JsonReaderWithMemory

class OptionDeserializer extends ssserializer.deserializers.json.OptionDeserializer[JsonReaderWithMemory] {

  override def buildFinalOption(element: Any, jsonInput: JsonReaderWithMemory): Option[_] = {
    // resolve ref dependencies
    val realElement = jsonInput.topLevelArrayDeserializer.deserializeRefDependencies(Seq(element), jsonInput)
    // build the final option
    super.buildFinalOption(realElement.head, jsonInput)
  }

}
