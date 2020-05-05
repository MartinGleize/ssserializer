package ssserializer.deserializers.json.compact

import java.io.StringReader

import ssserializer.deserializers.json.Person
import ssserializer.deserializers.json.parsing.ReallyFastJsonReader
import ssserializer.serializers.json.compact.CompactJsonSerializerTest
import ssserializer.{TestObjects, UnitSpec}

import scala.reflect.runtime.universe._


class CompactJsonDeserializerTest extends UnitSpec {

  val deserializer = new CompactJsonDeserializer()

  // used only to get the test case serializations
  val serializationTest = new CompactJsonSerializerTest()

  "A CompactJsonDeserializer" should "correctly deserialize simple map objects" in {
    for ((testCase, expected) <- serializationTest.expectedSerializationsMaps.zip(TestObjects.mapTests)) {
      deserialize[Map[String, Seq[Int]]](testCase) should be (expected)
    }
  }

  it should "correctly deserialize composite objects with tuples and options" in {
    for ((testCase, expected) <- serializationTest.expectedSerializationTuples.zip(TestObjects.tuple3Tests)) {
      deserialize[(String, Option[Int], Person)](testCase) should be (expected)
    }
  }

  def deserialize[T : TypeTag](serialization: String): T = {
    // deserialize
    val input = new StringReader(serialization)
    val jsonReader = new ReallyFastJsonReader(input)
    val res = deserializer.deserialize(typeOf[T], jsonReader)
    input.close()
    res.asInstanceOf[T]
  }

}