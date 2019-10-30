package ssserializer.typing

import java.util

import scala.collection.mutable
import scala.reflect.runtime.universe._

import scala.collection.JavaConversions._

/**
 * Performs mappings from types to arbitrary elements.
 * @tparam E the element type
 * @author mgleize
 */
class TypeMapper[E] {

  private val cache: mutable.Map[Type, Option[E]] = new util.IdentityHashMap[Type, Option[E]]()

  def map(t: Type, detectedObjects: Seq[(Detector, E)]): Option[E] = {
    cache.getOrElseUpdate(t, {
      val tDealias = t.dealias
      detectedObjects
        .find { case (detector, _) => detector.isCompatible(tDealias) }
        .map(_._2)
    })
  }

}
