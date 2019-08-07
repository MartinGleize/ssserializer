package ssserializer.serializers

import java.io.OutputStream

import ssserializer.typing.TypeMapper
import ssserializer.typing._

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

trait AnySerializer extends Serializer[Any] {

  override def serialize(data: Any, t: Type, dest: OutputStream): Unit = TypeMapper.map(t) match {
    case None => throw new RuntimeException("Type not handled") // TODO: throw some custom exception
    case Some(serializableType) => serializableType match {
      case Double =>
        doubleSerializer.serialize(data.asInstanceOf[Double], t, dest, this)
      case Long =>
        longSerializer.serialize(data.asInstanceOf[Long], t, dest, this)
      case Int =>
        intSerializer.serialize(data.asInstanceOf[Int], t, dest, this)
      case Boolean =>
        booleanSerializer.serialize(data.asInstanceOf[Boolean], t, dest, this)
      case Seq =>
        seqSerializer.serialize(data.asInstanceOf[Seq[_]], t, dest, this)
      case Map =>
        mapSerializer.serialize(data.asInstanceOf[Map[_, _]], t, dest, this)
      case CaseClass =>
        caseClassSerializer.serialize(data.asInstanceOf[Product], t, dest, this)
    }
  }

  override def serialize(data: Any, t: universe.Type, dest: OutputStream, anySerializer: AnySerializer): Unit = {
    // TODO: throw exception to warn that this shouldn't be used on anyserializer
    ()
  }

  def doubleSerializer: Serializer[Double]

  def longSerializer: Serializer[Long]

  def intSerializer: Serializer[Int]

  def booleanSerializer: Serializer[Boolean]

  def seqSerializer: Serializer[Seq[_]]

  def mapSerializer: Serializer[Map[_, _]]

  def caseClassSerializer: Serializer[Product]

}
