package ssserializer.typing

import scala.reflect.runtime.universe._

trait Detector {

  def isCompatible(t: Type): Boolean
}

object Detector {

  def ofExactType(t: Type): Detector = _ =:= t

  def ofExactBaseType(baseType: Type): Detector = isDerivedFrom(_, baseType)

  def ofBaseErasure(baseType: Type): Detector = isDerivedFrom(_, baseType.erasure)

  /** Returns true if tpe is derived from baseType */
  def isDerivedFrom(tpe: Type, baseType: Type): Boolean = {
    tpe.baseClasses.contains(baseType.typeSymbol)
  }
}
