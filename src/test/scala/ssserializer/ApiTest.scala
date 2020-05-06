package ssserializer

import java.io.{ByteArrayInputStream, ByteArrayOutputStream, InputStream, OutputStream}

import org.scalatest.Assertion
import ssserializer.TestObjects.{arrayAnyTests, arrayIntTests, arrayStringTests, booleanTests, caseClassDefaultParameterTests, caseClassMoreTests, caseClassTests, caseClassWithExtraValues, doubleTests, intTests, listTests, mapTests, optionTests, sequenceTests, setTests, stringTests, tuple2Tests, tuple3Tests, vectorTests}
import ssserializer.deserializers.Deserializer
import ssserializer.serializers.Serializer

import scala.reflect.runtime.universe._

abstract class ApiTest(val testSuitName: String)(implicit val serializer: Serializer[OutputStream],
                                                 implicit val deserializer: Deserializer[Any, InputStream])
  extends UnitSpec {

  s"The full ${testSuitName} ssserializer API" should "work correctly on a wide range of common types" in {
    intTests.foreach(test(_))
    doubleTests.foreach(test(_))
    booleanTests.foreach(test(_))
    stringTests.foreach(test(_))
    arrayIntTests.foreach(test(_))
    arrayStringTests.foreach(test(_))
    // TODO: this one fails randomly, seemingly the first time, and then not anymore, (might have to do with Java arrays not being true generics)
    arrayAnyTests.foreach(test(_))
    sequenceTests.foreach(test(_))
    listTests.foreach(test(_))
    vectorTests.foreach(test(_))
    setTests.foreach(test(_))
    mapTests.foreach(test(_))
    caseClassTests.foreach(test(_))
    caseClassMoreTests.foreach(test(_))
    caseClassDefaultParameterTests.foreach(test(_))
    caseClassWithExtraValues.foreach(test(_))
    optionTests.foreach(test(_))
    tuple2Tests.foreach(test(_))
    tuple3Tests.foreach(test(_))
  }

  def test[T : TypeTag](data: T): Assertion = {
    serializeDeserialize(data) should be (data)
  }

  def serializeDeserialize[T : TypeTag](data: T): T = {
    // create the transitional stream (a byte buffer)
    val output = new ByteArrayOutputStream()
    // serialize (this is the public API, which uses an implicit serializer)
    serialize(data, output)
    // just for debugging
    val stringOutput = output.toString("UTF-8")
    // deserialize (also the public API, using an implicit deserializer)
    val input = new ByteArrayInputStream(output.toByteArray)
    deserialize[T](input)
  }

}
