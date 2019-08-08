package ssserializer.deserializers.json

import java.io.{Reader, StringReader}
import java.nio.charset.Charset
import java.util.Scanner
import java.util.regex.Pattern

import org.apache.commons.text.StringEscapeUtils

class JsonReader(val in: Reader) {

  def this(s: String) { this(new StringReader(s)) }

  val scanner: Scanner = {
    val scanner = new Scanner(in)
    //scanner.useDelimiter(JsonReader.DELIMITER)
    scanner
  }

  def readJsonName(): String = {
    val res = readJsonString()
    skipAfter(JsonReader.COLON)
    res
  }

  def readJsonString(): String = {
    // find the next start of a string
    skipAfter(JsonReader.STRING_START)
    // read until the next "
    val res = stringUntil(JsonReader.STRING_END)
    // move until after the "
    skipAfter(JsonReader.STRING_START)
    // escape
    StringEscapeUtils.unescapeJson(res)
  }

  def readJsonNumber(): String = {
    val res = next(JsonReader.NUMBER)
    res.toString
  }

  def readJsonBoolean(): String = {
    scanner.next(JsonReader.BOOLEAN)
  }

  private def stringUntil(pattern: Pattern): String = {
    scanner.useDelimiter("")
    val res = if (scanner.hasNext(pattern)) {
      // the delimiter has been found right away, meaning that the desired string is empty
      ""
    } else {
      scanner.useDelimiter(pattern)
      scanner.next()
    }
    reset()
    res
  }

  private def next(pattern: Pattern): String = {
    scanner.findWithinHorizon(pattern, 0)
  }

  def skipAfter(pattern: Pattern): Unit = {
    scanner.findWithinHorizon(pattern, 0)
  }

  private def reset(): Unit = {
    scanner.reset()
    //scanner.useDelimiter(JsonReader.DELIMITER)
  }
}

object JsonReader {
  val DELIMITER = "\\s*:?\\s*"
  val STRING_START = Pattern.compile("\"")
  val STRING_END = Pattern.compile("(?<!\\\\)\"")
  //val NUMBER = Pattern.compile("[0-9]+")
  //val NUMBER = Pattern.compile("[0-9]+\\.[0-9]+")
  val NUMBER = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?")
  val BOOLEAN = Pattern.compile("(true)|(false)")
  val COLON = Pattern.compile(":")
  val CURLY_OPEN = Pattern.compile("\\{")
  val CURLY_CLOSE = Pattern.compile("\\}")
  val BRACKET_OPEN = Pattern.compile("\\[")
  val BRACKET_CLOSE = Pattern.compile("\\]")

  def p(s: String): Pattern = Pattern.compile(s)
}

object JsonReaderApp extends App {
  println("Hi Bro!")
  println()
  println(new JsonReader("\"lol\" hohoho").readJsonString())
  println(new JsonReader("     \"lol\\\"lol\" hohoho").readJsonString())
}