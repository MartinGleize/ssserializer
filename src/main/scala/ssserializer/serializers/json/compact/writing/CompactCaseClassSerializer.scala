package ssserializer.serializers.json.compact.writing

import java.io.BufferedWriter

import ssserializer.serializers.json.CaseClassSerializer

import scala.reflect.runtime.universe

class CompactCaseClassSerializer extends CaseClassSerializer {

  /** Instead of outputting the arg name like in the original serializer, just output simples IDs */
  override def outputBeforeProductArg(argName: String, argIndex: Int, argType: universe.Type, output: BufferedWriter): Unit = {
    output.write("\"" + String.valueOf(argIndex) + "\":")
  }

}
