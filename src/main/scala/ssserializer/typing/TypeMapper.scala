package ssserializer.typing

import scala.reflect.runtime.universe._
import detectors._

object TypeMapper {

  def map[E](t: Type, detectedObjects: Seq[(Detector, E)]): Option[E] = {
    val tDealias = t.dealias
    detectedObjects
      .find { case (detector, _) => detector.isCompatible(tDealias) }
      .map(_._2)
  }

}
