package ssserializer.serializers.generic

import ssserializer.serializers.MasterSerializer

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

/**
 * Case class serializer. A current limitation is that it doesn't handle inner case classes (which could typically be
 * considered standalone classes in most cases).
 */
trait CaseClassSerializer[Output] extends NullHandlingSerializer[Product, Output] {

  /** Happens before any element of the product is serialized */
  def outputStart(output: Output): Unit

  /** Happens after all elements of the product have been serialized (even if product was empty) */
  def outputEnd(output: Output): Unit

  /** Happens before each element of the product gets serialized: it's a good occasion to output the name of the element usually */
  def outputBeforeProductArg(argName: String, argIndex: Int, argType: universe.Type, output: Output): Unit

  /** Happens after an element of the product has been serialized */
  def outputAfterProductArg(arg: Any, argType: Type, output: Output, hasNextProductArg: Boolean): Unit

  override def serializeNonNull(product: Product, t: Type, output: Output, parentSerializer: MasterSerializer[Output]): Unit = {
    // map type params (like "A") to actual type arguments
    val typeMap = CaseClassSerializer.genericTypeMap(t)
    // get the case class arguments
    val parameterSymbols = CaseClassSerializer.parameterSymbols(t)
    val orderedArgs = parameterSymbols.map(s => s.name -> s.info)
    val productArgs = product.productIterator.toList
    val size = productArgs.size
    // serialize
    outputStart(output)
    for ((arg, index) <- productArgs.zipWithIndex) {
      val argName = orderedArgs(index)._1.toString
      val paramType = orderedArgs(index)._2
      val argType = typeMap.getOrElse(paramType.typeSymbol, paramType)
      outputBeforeProductArg(argName, index, argType, output)
      parentSerializer.serialize(arg, argType, output)
      outputAfterProductArg(arg, argType, output, index != size - 1)
    }
    outputEnd(output)
  }
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

  def genericTypeMap(caseClassType: Type): Map[Symbol, Type] = {
    val genericTypeSymbols = caseClassType.typeSymbol.typeSignature.typeParams
    val actualTypes = caseClassType.typeArgs
    Map(genericTypeSymbols.zip(actualTypes):_*)
  }
}
