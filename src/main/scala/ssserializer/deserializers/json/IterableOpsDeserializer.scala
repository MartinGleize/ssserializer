package ssserializer.deserializers.json

import ssserializer.deserializers.json.parsing.JsonReader

import scala.collection.generic.CanBuildFrom
import scala.collection.mutable
import scala.reflect.runtime.universe.Type

/**
 * JSON Deserializer for any seq-like type that's encoded as a JSON array
 *
 * @param factory a suitable CanBuildFrom to build the target collection type
 * @tparam CC deserialized collection type
 */
class IterableOpsDeserializer[CC[A] <: Iterable[A], JsonInput <: JsonReader](implicit factory: CanBuildFrom[Nothing, Any, CC[Any]])
  extends SeqDeserializer[CC[Any], JsonInput] {

  override def buildFinalObject(elements: Seq[Any], t: Type, jsonReader: JsonInput): CC[Any] = {
    val builder = factory()
    for (elt <- elements) {
      builder += elt
    }
    builder.result()
  }

}


/* // Scala 2.13.0 implementation

import scala.collection.{Factory, IterableOps, mutable}

class IterableOpsDeserializer[CC[A] <: Iterable[A] with IterableOps[A, CC, _]]()(implicit factory: Factory[Any, CC[Any]]) extends SeqDeserializer[CC[Any]] {

  override def constructFinalObject(elements: mutable.Seq[Any]): CC[Any] = {
    factory.fromSpecific(elements)
  }

}

*/