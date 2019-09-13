package ssserializer.deserializers.json

class CaseClassDeserializerTest extends JsonDeserializerSpec {

  "A JSON CaseClassDeserializer" should "deserialize case class objects in a canonical JSON encoding (with simple child types)" in {
    val checks: Seq[(String, Person)] = Seq(
      "{\"name\":\"John\", \"age\":35}" -> Person("John", 35),
      "{ \"name\":\"Julie\",\"age\" : 28 }" -> Person("Julie", 28),
      "null" -> null
    )
    checks.foreach { case (input, output) =>
      assertEncodedStringSameAsDeserializedValue(input, output)
    }
  }
}

case class Person(name: String, age: Int)
case class BasketballTeam(name: String, players: Seq[Person])
case class EmployedPerson(name: String, hasJob: Boolean = false)
object EmployedPerson {
  override def toString: String = "EmployedPerson companion object"
}
