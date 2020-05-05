package ssserializer.deserializers.json.compact.toplevel

import ssserializer.deserializers.json.compact.JsonReaderWithMemory

class TupleDeserializer extends ssserializer.deserializers.json.TupleDeserializer[JsonReaderWithMemory]
  with SeqDeserializerMixin[Product]