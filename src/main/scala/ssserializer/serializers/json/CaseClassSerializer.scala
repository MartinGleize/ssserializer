package ssserializer.serializers.json

import java.io.BufferedWriter

import ssserializer.deserializers.json.parsing.JsonUtil

import scala.reflect.runtime.universe

/**
 * Case class serializer. A current limitation is that it doesn't handle inner case classes (which could typically be
 * considered standalone classes in most cases).
 */
class CaseClassSerializer extends ssserializer.serializers.generic.CaseClassSerializer[BufferedWriter]
  with NullHandlingSerializer[Product] {

  /** Happens before any element of the product is serialized */
  override def outputStart(w: BufferedWriter): Unit = {
    w.write("{")
  }

  /** Happens after all elements of the product have been serialized (even if product was empty) */
  override def outputEnd(w: BufferedWriter): Unit = {
    w.write("}")
  }

  /** Happens before each element of the product gets serialized: it's a good occasion to output the name of the element usually */
  override def outputBeforeProductArg(argName: String, argType: universe.Type, w: BufferedWriter): Unit = {
    val jsonifiedArgName = jsonifyArgName(argName)
    w.write("\"" + jsonifiedArgName + "\":")
  }

  /** Happens after an element of the product has been serialized */
  override def outputAfterProductArg(arg: Any, argType: universe.Type, w: BufferedWriter, hasNextProductArg: Boolean): Unit = {
    if (hasNextProductArg) {
      w.write(",")
    }
  }

  private def jsonifyArgName(argName: String): String = JsonUtil.escape(argName.trim)
}
