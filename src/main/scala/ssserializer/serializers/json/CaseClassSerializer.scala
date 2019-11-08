package ssserializer.serializers.json

import ssserializer.deserializers.json.parsing.JsonUtil
import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe._

/**
 * Case class serializer. A current limitation is that it doesn't handle inner case classes (which could typically be
 * considered standalone classes in most cases).
 */
class CaseClassSerializer extends Serializer[Product] {

  override def serializeNonNull(product: Product, t: Type, w: Writer, parentSerializer: MasterSerializer[Writer]): Unit = {
    val parameterSymbols = CaseClassSerializer.parameterSymbols(t)
    val orderedArgs = parameterSymbols.map(s => s.name -> s.info)
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

  private def jsonifyArgName(argName: String): String = JsonUtil.escape(argName.trim)
}

object CaseClassSerializer {

  /** Return all the parameter symbols, in order, in the primary constructor of a case class */
  def parameterSymbols(caseClass: Type): Seq[Symbol] = {
    // get exclusively the "case accessors", i.e. the accessors automatically created for the parameters of the main constructor
    val caseAccessors = caseClass.members.sorted
      .filter(_.isInstanceOf[MethodSymbol])
      .map(_.asInstanceOf[MethodSymbol])
      .filter(_.isCaseAccessor)
    // build a set of all their names
    val accessorNames = caseAccessors
      .map(_.name.toString.trim)
      .toSet
    // get the type symbols of the non-methods exactly named that way (these will be the types of the case class parameters)
    caseClass.members.sorted
      .filter(!_.isMethod)
      .filter(m => accessorNames.contains(m.name.toString.trim))
  }

}
