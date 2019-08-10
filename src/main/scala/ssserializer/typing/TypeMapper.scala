package ssserializer.typing

import scala.reflect.runtime.universe._
import detectors._

object TypeMapper {

  private val detectors: Seq[(Detector, SerializableType)] = Vector(
    doubleDetector -> Double,
    longDetector -> Long,
    intDetector -> Int,
    booleanDetector -> Boolean,
    stringDetector -> String,
    seqDetector -> Seq,
    mapDetector -> Map,
    caseClassDetector -> CaseClass
  )

  def map(t: Type): Option[SerializableType] = map(t, detectors)

  def map[E](t: Type, detectedObjects: Seq[(Detector, E)]): Option[E] = {
    detectedObjects
      .find { case (detector, _) => detector.isCompatible(t) }
      .map(_._2)
  }

}
