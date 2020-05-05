package ssserializer.deserializers.json.compact.toplevel

import ssserializer.deserializers.json.compact.JsonReaderWithMemory

import scala.collection.generic.CanBuildFrom

class IterableOpsDeserializer[CC[A] <: Iterable[A]](implicit factory: CanBuildFrom[Nothing, Any, CC[Any]])
  extends ssserializer.deserializers.json.IterableOpsDeserializer[CC, JsonReaderWithMemory]
    with SeqDeserializerMixin[CC[Any]]