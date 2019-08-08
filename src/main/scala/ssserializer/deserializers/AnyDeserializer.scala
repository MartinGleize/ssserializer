package ssserializer.deserializers

import java.io.InputStream

import ssserializer.typing._

import scala.reflect.runtime.universe
import scala.reflect.runtime.universe._

trait AnyDeserializer[Input] extends Deserializer[Any, Input] {

  override def deserialize(t: Type, src: Input): Any = TypeMapper.map(t) match {
    case None => throw new RuntimeException("Type not handled") // TODO: throw some custom exception
    case Some(serializableType) => serializableType match {
      case Double =>
        doubleDeserializer.deserialize(t, src, this)
      case Long =>
        longDeserializer.deserialize(t, src, this)
      case Int =>
        intDeserializer.deserialize(t, src, this)
      case Boolean =>
        booleanDeserializer.deserialize(t, src, this)
      case String =>
        stringDeserializer.deserialize(t, src, this)
      case Seq =>
        seqDeserializer.deserialize(t, src, this)
      case Map =>
        mapDeserializer.deserialize(t, src, this)
      case CaseClass =>
        caseClassDeserializer.deserialize(t, src, this)
    }
  }

  override def deserialize(t: universe.Type, src: Input, parentDeserializer: AnyDeserializer[Input]): Unit = {
    // TODO: throw exception to warn that this shouldn't be used on anyserializer
    ()
  }

  def doubleDeserializer: Deserializer[Double, Input]

  def longDeserializer: Deserializer[Long, Input]

  def intDeserializer: Deserializer[Int, Input]

  def booleanDeserializer: Deserializer[Boolean, Input]

  def stringDeserializer: Deserializer[String, Input]

  def seqDeserializer: Deserializer[Seq[_], Input]

  def mapDeserializer: Deserializer[Map[_, _], Input]

  def caseClassDeserializer: Deserializer[Product, Input]

}
