package ssserializer.codecs

import java.io.{BufferedWriter, StringReader, StringWriter}

import ssserializer.deserializers.json.JsonReader

import scala.collection.Factory
import scala.collection.mutable.ArrayBuffer

//import scala.reflect.runtime.universe._

trait Encodable[T] {
  def toJson(implicit encoder: Encoder[T, BufferedWriter]): String
}

trait Decodable {
  def as[T](implicit decoder: Decoder[T, JsonReader]): T
}

trait Encoder[T, Out] {

  def encode(o: T, out: Out): Unit
}

class AnyValEncoder[T <: AnyVal] extends Encoder[T, BufferedWriter] {
  override def encode(o: T, out: BufferedWriter): Unit = {
    out.write(o.toString)
  }
}

class IterableEncoder[CC[X] <: Iterable[X], A]()(implicit encoder: Encoder[A, BufferedWriter]) extends Encoder[CC[A], BufferedWriter] {
  override def encode(o: CC[A], out: BufferedWriter): Unit = {
    out.write("[")
    // might not be efficient, you can do it with an iterator instead
    if (o.nonEmpty) {
      for (dude <- o.init) {
        encoder.encode(dude, out)
        out.write(", ")
      }
      encoder.encode(o.last, out)
    }
    out.write("]")
  }
}

trait Decoder[T, In] {
  def decode(in: In): T
}

class IntDecoder extends Decoder[Int, JsonReader] {
  override def decode(in: JsonReader): Int = {
    in.readJsonNumber().toInt
  }
}

class IterableDecoder[CC[X] <: Iterable[X], A]()(implicit decoder: Decoder[A, JsonReader], factory: Factory[A, CC[A]]) extends Decoder[CC[A], JsonReader] {
  override def decode(in: JsonReader): CC[A] = {
    in.skipAfter(JsonReader.BRACKET_OPEN)
    val res = new ArrayBuffer[A]()
    var potentialElement: Option[A] = null
    while ({potentialElement = deserializeNextElement(in); potentialElement.isDefined}) {
      val element = potentialElement.get
      res += element
    }
    factory.fromSpecific(res)
  }

  def deserializeNextElement(jsonReader: JsonReader): Option[A] = {
    // first try to look for the end of the JSON array
    if (jsonReader.tryToConsumeNextToken(JsonReader.BRACKET_CLOSE)) {
      None
    } else {
      // can't read a "]" so a new element can be read
      val element = decoder.decode(jsonReader)
      // try to read a "," after (if it fails it likely means this would be the last element)
      jsonReader.tryToConsumeNextToken(JsonReader.COMMA)
      Some(element)
    }
  }
}

/**
 * This demonstrates strong compile-time type-checking in encoding-
 * decoding of Int and
 * Iterable-like collections
 */
object Main extends App {
  implicit def intEncoder[T <: AnyVal]: Encoder[T, BufferedWriter] = new AnyValEncoder[T]()
  implicit def iterableEncoder[CC[X] <: Iterable[X], A](implicit encoder: Encoder[A, BufferedWriter]): Encoder[CC[A], BufferedWriter] = new IterableEncoder[CC, A]()
  implicit def intDecoder: Decoder[Int, JsonReader] = new IntDecoder()
  implicit def iterableDecoder[CC[X] <: Iterable[X], A](implicit decoder: Decoder[A, JsonReader], factory: Factory[A, CC[A]]): Decoder[CC[A], JsonReader] = new IterableDecoder[CC, A]()
  implicit def anyToEncodable[T](o: T): Encodable[T] = {
    new Encodable[T] {
      override def toJson(implicit encoder: Encoder[T, BufferedWriter]): String = {
        val sw = new StringWriter()
        val bw = new BufferedWriter(sw)
        encoder.encode(o, bw)
        bw.close()
        sw.toString
      }
    }
  }
  implicit def stringToDecodable(json: String): Decodable = {
    new Decodable {
      override def as[T](implicit decoder: Decoder[T, JsonReader]): T = {
        val sr = new StringReader(json)
        val in = new JsonReader(sr)
        val res = decoder.decode(in)
        in.close()
        res
      }
    }
  }

  val jsonInt: String = 2.toJson
  val jsonList: String = List(1, 2, 3, 4).toJson
  val jsonVector: String = Vector(1, 2, 3, 4).toJson
  //val jsonMap: String = Map(1 -> 1, 2 -> 2).toJson // this should fail with implicit encoder not found for Map
  println(jsonInt)
  println(jsonList)
  println(jsonVector)
  // println(jsonInt.as[Vector[Int]]) // throws a runtime error
  println(jsonInt.as[Int])
  println(jsonList.as[List[Int]]) // again the Scala IntelliJ plugin falsely shows an error here on higher-kind implicits
  println(jsonVector.as[Vector[Int]])
  val composedDeeper = Vector(List(11, 12), List(21, 22))
  val jsonComposed = composedDeeper.toJson
  println(jsonComposed)
  println(jsonComposed.as[Vector[List[Int]]] == composedDeeper)
}