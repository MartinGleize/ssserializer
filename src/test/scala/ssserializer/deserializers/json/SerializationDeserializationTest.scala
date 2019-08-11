package ssserializer.deserializers.json

import java.io.{BufferedWriter, ByteArrayInputStream, ByteArrayOutputStream, InputStreamReader, OutputStreamWriter, StringWriter}
import java.nio.charset.StandardCharsets

import org.scalatest.Assertion
import ssserializer.serializers.json.JsonSerializer

import scala.reflect.runtime.universe._

class SerializationDeserializationTest extends JsonDeserializerSpec {

  val serializer = new JsonSerializer()
  val deserializer = new JsonDeserializer()

  val int0: Int = 0
  val int1337: Int = 1337
  val intn1: Int = -1
  val double0: Double = 0.0
  val double1337: Double = 1.337
  val doublen1: Double = -1.0
  val stringNull: String = null
  val stringEmpty = ""
  val stringSimple = "lol"
  val stringToEscapse = "lol\"lol"
  val stringWithLinebreak = "lol\nlol"

  "A JSON serialization/deserialization sequence" should "return the same object" in {
    test(int0)
    test(int1337)
    test(intn1)
    test(double0)
    test(double1337)
    test(doublen1)
    test(true)
    test(false)
    test(stringNull)
    test(stringEmpty)
    test(stringSimple)
    test(stringToEscapse)
    test(stringWithLinebreak)
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
