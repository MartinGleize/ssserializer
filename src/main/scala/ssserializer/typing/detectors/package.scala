package ssserializer.typing

import scala.reflect.runtime.universe._

/**
  * Type detectors for common Scala types
  */
package object detectors {

  /*########### VALUE TYPES ##############*/

  val doubleDetector: Detector = _ =:= typeOf[Double]

  val floatDetector: Detector = _ =:= typeOf[Float]

  val longDetector: Detector = _ =:= typeOf[Long]

  val intDetector: Detector = _ =:= typeOf[Int]

  val shortDetector: Detector = _ =:= typeOf[Short]

  val byteDetector: Detector = _ =:= typeOf[Byte]

  val charDetector: Detector = _ =:= typeOf[Char]

  val unitDetector: Detector = _ =:= typeOf[Unit]

  val booleanDetector: Detector = _ =:= typeOf[Boolean]

  /*########### COLLECTIONS ############## */

  val stringDetector: Detector = Detector.ofExactType(typeOf[String])

  val seqDetector: Detector = Detector.ofBaseErasure(typeOf[Seq[_]])

  val mapDetector: Detector = Detector.ofBaseErasure(typeOf[Map[_, _]])

  val optionDetector: Detector = Detector.ofBaseErasure(typeOf[Option[_]])

  val caseClassDetector: Detector = _.typeSymbol.asClass.isCaseClass
}
