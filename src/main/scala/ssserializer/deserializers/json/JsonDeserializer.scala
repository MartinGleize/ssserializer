package ssserializer.deserializers.json

import ssserializer.deserializers.AnyDeserializer

class JsonDeserializer extends AnyDeserializer[JsonReader] {

  override val doubleDeserializer: Deserializer[Double] = defaults.doubleDeserializer

  override val longDeserializer: Deserializer[Long] = defaults.longDeserializer

  override val intDeserializer: Deserializer[Int] = defaults.intDeserializer

  override val booleanDeserializer: Deserializer[Boolean] = defaults.booleanDeserializer

  override def stringDeserializer: Deserializer[String] = defaults.stringDeserializer

  override val seqDeserializer: Deserializer[Seq[_]] = new SeqDeserializer()

  override def mapDeserializer: Deserializer[Map[_, _]] = new MapDeserializer()

  override def caseClassDeserializer: Deserializer[Product] = new CaseClassDeserializer()
}
