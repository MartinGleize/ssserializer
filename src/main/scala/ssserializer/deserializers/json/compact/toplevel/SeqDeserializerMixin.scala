package ssserializer.deserializers.json.compact.toplevel

import ssserializer.deserializers.json.compact.JsonReaderWithMemory

import scala.reflect.runtime.universe

trait SeqDeserializerMixin[S] extends ssserializer.deserializers.json.SeqDeserializer[S, JsonReaderWithMemory] {

  abstract override def buildFinalObject(elements: Seq[Any], jsonInput: JsonReaderWithMemory): S = {
    // resolve ref dependencies
    val realElements = jsonInput.topLevelArrayDeserializer.deserializeRefDependencies(elements, jsonInput)
    // build the final iterable
    super.buildFinalObject(realElements, jsonInput)
  }

  override def buildFinalObject(elements: Seq[Any], t: universe.Type, jsonInput: JsonReaderWithMemory): S = {
    // resolve ref dependencies
    val realElements = jsonInput.topLevelArrayDeserializer.deserializeRefDependencies(elements, jsonInput)
    // build the final iterable
    super.buildFinalObject(realElements, t, jsonInput)
  }
}
