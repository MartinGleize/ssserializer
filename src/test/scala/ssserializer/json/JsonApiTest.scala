package ssserializer.json

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import org.scalatest.Assertion
import ssserializer.TestObjects._
import ssserializer.UnitSpec

import scala.reflect.runtime.universe._

// this import exposes the public API
import ssserializer._
// the ssserializer.json package provides the implicit JSON serializer/deserializer.

class JsonApiTest extends ApiTest("JSON") {
  // the ssserializer.json package object provides the implicit compact JSON serializer/deserializer
  // the test cases come from the super class
}
