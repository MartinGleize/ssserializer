package ssserializer.deserializers.json

import java.io.{ByteArrayInputStream, StringReader}
import java.nio.charset.StandardCharsets

import org.scalatest.{FlatSpec, Matchers}

import scala.reflect.runtime.universe._

class SeqDeserializerTest extends FlatSpec with Matchers {

  "A JSON SeqDeserializer" should "deserialize JSON sequences correctly (with simple parameter types)" in {
    val checks = Seq(
      " {\"size\":2,\"seq\":[1,2]}" -> Seq(1, 2),
      "{ \"size\" : 3, \"seq\" : [1,2, 3]}" -> Seq(1, 2 ,3)
    )
    checks.foreach { case (input, output) =>
      getSequence(input) should be (output)
    }
  }

  def getSequence(s: String): Seq[Int] = {
    val deserializer = new JsonDeserializer()
    val t = typeOf[Seq[Int]]
    val res = deserializer.deserialize(t, new JsonReader(new StringReader(s)))
    res.asInstanceOf[Seq[Int]]
  }
}
