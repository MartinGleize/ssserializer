package ssserializer.deserializers.json

import java.io.{BufferedWriter, ByteArrayInputStream, ByteArrayOutputStream, InputStreamReader, OutputStreamWriter, StringWriter}
import java.nio.charset.StandardCharsets

import org.scalatest.Assertion
import ssserializer.serializers.json.JsonSerializer
import ssserializer.TestObjects._

import scala.reflect.runtime.universe._

class SerializationDeserializationTest extends JsonDeserializerSpec {

  val serializer = new JsonSerializer()
  val deserializer = new JsonDeserializer()



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
