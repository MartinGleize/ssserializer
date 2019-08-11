package ssserializer.deserializers.json

import java.io.{BufferedWriter, ByteArrayInputStream, ByteArrayOutputStream, InputStreamReader, OutputStreamWriter, StringWriter}
import java.nio.charset.StandardCharsets

import org.scalatest.Assertion
import ssserializer.serializers.json.JsonSerializer

import scala.reflect.runtime.universe._

class SerializationDeserializationTest extends JsonDeserializerSpec {

  val serializer = new JsonSerializer()
  val deserializer = new JsonDeserializer()

  val intTests: Seq[Int] = Seq(0, 1337, -1)
  val doubleTests: Seq[Double] = Seq(0.0, 1.337, -1.337)
  val booleanTests: Seq[Boolean] = Seq(true, false)
  val stringTests: Seq[String] = Seq(null, "", "lol", "lol\"lol", "lol\nlol")
  val sequenceTests: Seq[Seq[Seq[String]]] = Seq(
    Seq(),
    Seq(Seq()),
    Seq(Seq("")),
    Seq(Seq("11", "12"), Seq("21"))
  )
  val mapTests: Seq[Map[String, Seq[Int]]] = Seq(
    Map(),
    Map("" -> Seq()),
    Map("key1" -> Seq(11, 12), "key2" -> Seq(21, 22, 23), "" -> null)
  )
  val caseClassTests: Seq[Seq[Person]] = Seq(
    Seq(),
    Seq(null),
    Seq(Person(null, -1)),
    Seq(Person("John", 26)),
    Seq(null, Person("Maria", 75), null, Person("Olaf", 54), Person("Bob", 40))
  )
  val caseClassMoreTests: Seq[BasketballTeam] = Seq(
    null,
    BasketballTeam("Lakers", Seq(Person("Lebron", 35), Person("AD", 24)))
  )
  val optionTests: Seq[Option[String]] = Seq(
    null,
    None,
    Some(null),
    Some("Haha")
  )

  "A JSON serialization/deserialization sequence" should "return the same object" in {
    intTests.foreach(test(_))
    doubleTests.foreach(test(_))
    booleanTests.foreach(test(_))
    stringTests.foreach(test(_))
    sequenceTests.foreach(test(_))
    mapTests.foreach(test(_))
    caseClassTests.foreach(test(_))
    caseClassMoreTests.foreach((test(_)))
    optionTests.foreach(test(_))
  }

  def test[T : TypeTag](data: T): Assertion = {
    serializeDeserialize(data) should be (data)
  }

  def serializeDeserialize[T : TypeTag](data: T): T = {
    // create the transitional stream (a byte buffer)
    val output = new ByteArrayOutputStream()
    val writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))
    // serialize
    serializer.serialize(data, writer)
    writer.close()
    // deserialize
    val input = new InputStreamReader(new ByteArrayInputStream(output.toByteArray), StandardCharsets.UTF_8)
    val jsonReader = new JsonReader(input)
    val res = deserializer.deserialize(typeOf[T], jsonReader)
    input.close()
    res.asInstanceOf[T]
  }
}
