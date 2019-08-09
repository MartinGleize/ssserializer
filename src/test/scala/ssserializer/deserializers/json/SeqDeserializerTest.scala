package ssserializer.deserializers.json

import java.io.{StringReader}

import org.scalatest.{FlatSpec, Matchers}

import scala.reflect.runtime.universe._

class SeqDeserializerTest extends FlatSpec with Matchers {

  "A JSON SeqDeserializer" should "deserialize JSON sequences correctly (with simple parameter types)" in {
    val checksInt: Seq[(String, Seq[Int])] = Seq(
      " [1,2]" -> Seq(1, 2),
      "[1,2, 3]" -> Seq(1, 2 ,3)
    )
    val checksBoolean = Seq(
      "[true, false, false]" -> Seq(true, false, false)
    )
    checksInt.foreach { case (input, output) =>
      assert(getSequence(input, output) == output)
    }
    checksBoolean.foreach { case (input, output) =>
      assert(getSequence(input, output) == output)
    }
  }

  def getSequence[T : TypeTag](s: String, expectedOutput: T): T = {
    val deserializer = new JsonDeserializer()
    val t = typeOf[T]
    val res = deserializer.deserialize(t, new JsonReader(new StringReader(s)))
    res.asInstanceOf[T]
  }
}
