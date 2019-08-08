package ssserializer.serializers.json

import ssserializer.serializers.{AnySerializer, Serializer}

class JsonSerializer extends AnySerializer {
  override val doubleSerializer: Serializer[Double] = new ToStringSerializer[Double]()

  override val longSerializer: Serializer[Long] = new ToStringSerializer[Long]()

  override val intSerializer: Serializer[Int] = new ToStringSerializer[Int]()

  override val booleanSerializer: Serializer[Boolean] = null // TODO: throw custom exception for non-implemented serializers

  override val stringSerializer: Serializer[String] = new StringSerializer()

  override val seqSerializer: Serializer[Seq[_]] = new SeqSerializer()

  override val mapSerializer: Serializer[Map[_, _]] = new MapSerializer()

  override val caseClassSerializer: Serializer[Product] = new CaseClassSerializer()
}
