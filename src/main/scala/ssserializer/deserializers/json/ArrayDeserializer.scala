package ssserializer.deserializers.json

import ssserializer.deserializers.json.parsing.JsonReader

import scala.reflect.ClassTag
import scala.reflect.runtime.universe._

class ArrayDeserializer[JsonInput <: JsonReader] extends SeqDeserializer[Array[Any], JsonInput] {

  override def buildFinalObject(elements: Seq[Any], t: Type, jsonInput: JsonInput): Array[Any] = {
    // use a class tag to create the correct type of array
    elements.toArray(typeToClassTag(t))
  }

  /** Converts the type information to a class tag */
  def typeToClassTag(t: Type): ClassTag[Any] = {
    ClassTag(ArrayDeserializer.mirror.runtimeClass(t))
  }
}

object ArrayDeserializer {
  private val mirror: Mirror = runtimeMirror(getClass.getClassLoader)
}
