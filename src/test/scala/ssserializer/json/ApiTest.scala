package ssserializer.json

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import org.scalatest.Assertion
import ssserializer.TestObjects._
import ssserializer.UnitSpec

import scala.reflect.runtime.universe._

// this import exposes the public API
import ssserializer._
// the ssserializer.json package provides the implicit JSON serializer/deserializer.

class ApiTest extends UnitSpec {

  intTests.foreach(test(_))
  doubleTests.foreach(test(_))
  booleanTests.foreach(test(_))
  stringTests.foreach(test(_))
  arrayIntTests.foreach(test(_))
  arrayStringTests.foreach(test(_))
  // TODO: this one fails randomly, seemingly the first time, and then not anymore,
  arrayAnyTests.foreach(test(_))
  sequenceTests.foreach(test(_))
  setTests.foreach(test(_))
  mapTests.foreach(test(_))
  caseClassTests.foreach(test(_))
  caseClassMoreTests.foreach((test(_)))
  optionTests.foreach(test(_))
  tuple2Tests.foreach(test(_))
  tuple3Tests.foreach(test(_))

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
