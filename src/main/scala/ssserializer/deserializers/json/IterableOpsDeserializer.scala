package ssserializer.deserializers.json

import scala.collection.{Factory, IterableOps, mutable}

class IterableOpsDeserializer[CC[A] <: Iterable[A] with IterableOps[A, CC, _]]()(implicit factory: Factory[Any, CC[Any]]) extends SeqDeserializer[CC[Any]] {

  override def constructFinalObject(elements: mutable.Seq[Any]): CC[Any] = {
    factory.fromSpecific(elements)
  }

}
