package ssserializer.deserializers.json

import ssserializer.deserializers.{AnyDeserializer, Deserializer}

class JsonDeserializer extends AnyDeserializer[JsonReader] {

  override val doubleDeserializer: Deserializer[Double, JsonReader] = defaults.doubleDeserializer

  override val longDeserializer: Deserializer[Long, JsonReader] = defaults.longDeserializer

  override val intDeserializer: Deserializer[Int, JsonReader] = defaults.intDeserializer

  override val booleanDeserializer: Deserializer[Boolean, JsonReader] = defaults.booleanDeserializer

  override def stringDeserializer: Deserializer[String, JsonReader] = throw new RuntimeException("Not implemented")

  override val seqDeserializer: Deserializer[Seq[_], JsonReader] = new SeqDeserializer()

  override def mapDeserializer: Deserializer[Map[_, _], JsonReader] = throw new RuntimeException("Not implemented")

  override def caseClassDeserializer: Deserializer[Product, JsonReader] = throw new RuntimeException("Not implemented")
}
