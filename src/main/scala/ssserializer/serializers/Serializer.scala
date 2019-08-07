package ssserializer.serializers

import java.io.OutputStream

import ssserializer.typing.TypeMapper
import ssserializer.typing._

import scala.reflect.runtime.universe._

trait Serializer {

  def serialize(o: Any, t: Type, dest: OutputStream): Unit = TypeMapper.map(t) match {
    case None => throw new RuntimeException("Type not handled") // TODO: throw some custom exception
    case Some(serializableType) => serializableType match {
      case Double =>
      case Long =>
      case Int =>
      case Boolean =>
      case Seq =>
      case Map =>
      case CaseClass =>
    }
  }

}
