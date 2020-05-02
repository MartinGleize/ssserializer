package ssserializer.serializers.json.compact

import java.io.{BufferedWriter, ByteArrayOutputStream, OutputStreamWriter}
import java.nio.charset.StandardCharsets

import ssserializer.{TestObjects, UnitSpec}

import scala.reflect.runtime.universe._

class CompactJsonSerializerTest extends UnitSpec {

  val serializer = new CompactJsonSerializer()

  /*
  val mapTests: Seq[Map[String, Seq[Int]]] = Seq(
    Map(),
    Map("" -> Seq()),
    Map("key1" -> Seq(11, 12), "key2" -> Seq(21, 22, 23), "" -> null)
  )
   */
  val expectedSerializationsMaps = Seq(
    "[[]]",
    "[[{\"k\":1,\"v\":2}],\"\",[]]",
    "[[{\"k\":1,\"v\":2},{\"k\":3,\"v\":4},{\"k\":5,\"v\":6}],\"key1\",[11,12],\"key2\",[21,22,23],\"\",null]"
  )

  "A CompactJsonSerializer" should "correct serialize simple map objects" in {
    for ((map, expected) <- TestObjects.mapTests.zip(expectedSerializationsMaps)) {
      serialize(map) should be (expected)
    }
  }

  def serialize[T : TypeTag](data: T): String = {
    // create the intermediate stream (a byte buffer)
    val output = new ByteArrayOutputStream()
    val writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8))
    // serialize
    serializer.serialize(data, writer)
    writer.close()
    output.toString
    /*
    // deserialize
    val input = new InputStreamReader(new ByteArrayInputStream(output.toByteArray), StandardCharsets.UTF_8)
    val jsonReader = new ScannerJsonReader(input)
    val res = deserializer.deserialize(typeOf[T], jsonReader)
    input.close()
    res.asInstanceOf[T]
    */
  }
}
