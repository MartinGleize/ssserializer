package ssserializer.deserializers.json

import org.scalatest.Assertion
import java.io.StringReader

import ssserializer.UnitSpec
import ssserializer.deserializers.json.parsing.{JsonReader, ScannerJsonReader}

import scala.reflect.runtime.universe._

trait JsonDeserializerSpec extends UnitSpec {

  def assertEncodedStringSameAsDeserializedValue[T : TypeTag](encoding: String, expectedOutput: T): Assertion = {
    val deserializer = new JsonDeserializer()
    val t = typeOf[T]
    val res = deserializer.deserialize(t, new ScannerJsonReader(new StringReader(encoding)))
    res.asInstanceOf[T] should be (expectedOutput)
  }

}