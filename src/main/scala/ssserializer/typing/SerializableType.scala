package ssserializer.typing

abstract sealed class SerializableType
// value types
case object Double extends SerializableType
case object Long extends SerializableType
case object Int extends SerializableType
case object Boolean extends SerializableType
// object types
case object String extends SerializableType
case object Seq extends SerializableType
case object Map extends SerializableType
case object CaseClass extends SerializableType