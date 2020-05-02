package ssserializer.typing

import java.util

import scala.collection.mutable
import scala.reflect.runtime.universe._

import scala.collection.JavaConversions._

/**
 * Performs mappings from types to arbitrary elements.
 * @tparam E the elements to associate to type detectors
 * @author mgleize
 */
class TypeMapper[E] {

  /** The cache uses reference equality/hashcode, for fast performance (and basically the same functionality under our assumptions) */
  private val cache: mutable.Map[Type, Option[(E, Type)]] = new util.IdentityHashMap[Type, Option[(E, Type)]]()

  /** Returns the element associated with the requested type 't', and a dealiased version of 't' */
  def map(t: Type, detectedObjects: Seq[(Detector, E)]): Option[(E, Type)] = {
    cache.getOrElseUpdate(t, {
      val tDealias = t.dealias
      detectedObjects
        .find { case (detector, _) => detector.isCompatible(tDealias) }
        .map(_._2) // this is the element (E) alone
        .map((_, tDealias)) // we add the type
    })
  }

}
