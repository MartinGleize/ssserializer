package ssserializer.serializers.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import org.apache.commons.text.StringEscapeUtils
import ssserializer.serializers.{MasterSerializer, Serializer}

import scala.reflect.runtime.universe

/**
 * Case class serializer. A current limitation is that it doesn't handle inner case classes (which could typically be
 * considered standalone classes in most cases).
 */
class CaseClassSerializer extends Serializer {

  override def serialize(data: Any, t: universe.Type, dest: OutputStream, parentSerializer: MasterSerializer): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest))
    val nonMethodMembers = t.members.sorted.filter(!_.isMethod)
    val orderedArgs = nonMethodMembers.map(s => s.name -> s.info)
    val product = data.asInstanceOf[Product]
    val productArgs = product.productIterator.toList
    val size = productArgs.size
    writer.write("{")
    for ((arg, index) <- productArgs.zipWithIndex) {
      val argName = jsonifyArgName(orderedArgs(index)._1.toString)
      writer.write("\"" + argName + "\":")
      parentSerializer.serialize(arg, orderedArgs(index)._2, dest)
      if (index != size - 1)
        writer.write(",")
    }
    writer.write("}")
    writer.flush()
  }

  private def jsonifyArgName(argName: String): String = StringEscapeUtils.escapeJson(argName.trim)
}
