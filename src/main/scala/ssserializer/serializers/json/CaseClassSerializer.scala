package ssserializer.serializers.json

import java.io.{BufferedWriter, OutputStream, OutputStreamWriter}

import org.apache.commons.text.StringEscapeUtils
import ssserializer.serializers.{AnySerializer, Serializer}

import scala.reflect.runtime.universe

class CaseClassSerializer extends Serializer[Product] {

  override def serialize(data: Product, t: universe.Type, dest: OutputStream, parentSerializer: AnySerializer): Unit = {
    val writer = new BufferedWriter(new OutputStreamWriter(dest))
    val nonMethodMembers = t.members.sorted.filter(!_.isMethod)
    val orderedArgs = nonMethodMembers.map(s => s.name -> s.info)
    val productArgs = data.productIterator.toList
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

  override def serialize(data: Product, t: universe.Type, dest: OutputStream): Unit = () // TODO: throw relevant exception

  private def jsonifyArgName(argName: String): String = StringEscapeUtils.escapeJson(argName.trim)
}
