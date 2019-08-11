package ssserializer.serializers.json

import org.apache.commons.text.StringEscapeUtils
import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

/**
 * Case class serializer. A current limitation is that it doesn't handle inner case classes (which could typically be
 * considered standalone classes in most cases).
 */
class CaseClassSerializer extends Serializer[Product] {

  override def serializeNonNull(product: Product, t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    val nonMethodMembers = t.members.sorted.filter(!_.isMethod)
    val orderedArgs = nonMethodMembers.map(s => s.name -> s.info)
    val productArgs = product.productIterator.toList
    val size = productArgs.size
    w.write("{")
    for ((arg, index) <- productArgs.zipWithIndex) {
      val argName = jsonifyArgName(orderedArgs(index)._1.toString)
      w.write("\"" + argName + "\":")
      parentSerializer.serialize(arg, orderedArgs(index)._2, w)
      if (index != size - 1)
        w.write(",")
    }
    w.write("}")
  }

  private def jsonifyArgName(argName: String): String = StringEscapeUtils.escapeJson(argName.trim)
}
