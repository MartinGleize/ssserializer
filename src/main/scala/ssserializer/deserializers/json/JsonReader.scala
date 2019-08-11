package ssserializer.deserializers.json

import java.io.{Reader, StringReader}
import java.util.Scanner
import java.util.regex.Pattern

import org.apache.commons.text.StringEscapeUtils

/**
 * Provides facilities to read JSON values in any Reader (often with an underlying InputStream).
 * Uses Scanner so Scanner-related restrictions apply (for example no two JsonReader reading on the same stream,
 * Scanner consumes the underlying stream so the behavior of a second Scanner reading is undefined).
 *
 * @param in
 */
class JsonReader(val in: Reader) {

  def this(s: String) { this(new StringReader(s)) }

  val scanner: Scanner = {
    val scanner = new Scanner(in)
    //scanner.useDelimiter(JsonReader.DELIMITER)
    scanner
  }

  /** Read the name of a name/value pair (consumes the colon ':' character too) */
  def readJsonName(): String = {
    val res = readJsonString()
    skipAfter(JsonReader.COLON)
    res
  }

  /** Read a JSON string value */
  def readJsonString(): String = {
    val res = next(JsonReader.STRING)
    // unescape
    StringEscapeUtils.unescapeJson(res)
  }

  // TODO: might be more efficient than the complex regex one
  private def readJsonStringOld(): String = {
    // find the next start of a string
    skipAfter(JsonReader.STRING_START)
    // read until the next "
    val res = if (tryToConsumeNextToken(JsonReader.STRING_START)) {
      // the delimiter has been found right away, meaning that the desired string is empty
      ""
    } else {
      scanner.useDelimiter(JsonReader.STRING_END)
      scanner.next()
    }
    reset()
    // move until after the "
    skipAfter(JsonReader.STRING_START)
    // escape
    StringEscapeUtils.unescapeJson(res)
  }

  /** Read a JSON numeric value */
  def readJsonNumber(): String = {
    val res = next(JsonReader.NUMBER)
    res.toString
  }

  /** Read a JSON boolean value */
  def readJsonBoolean(): String = {
    next(JsonReader.BOOLEAN)
  }

  private def next(pattern: Pattern): String = {
    scanner.findWithinHorizon(pattern, 0)
  }

  /** Jumps to the pattern and consumes it (ignores tokens in-between) */
  def skipAfter(pattern: Pattern): Unit = {
    scanner.findWithinHorizon(pattern, 0)
  }

  /** Tries to consume the next token (has to match the pattern), returns true if it was successful */
  def tryToConsumeNextToken(pattern: Pattern): Boolean = {
    try {
      // TODO: check this for performance, this seems inefficient
      consumeNextWhitespaces()
      scanner.skip(pattern)
      true
    } catch {
      case _: NoSuchElementException => false
    }
  }

  private def consumeNextWhitespaces(): Unit = {
    while (scanner.findWithinHorizon(JsonReader.WHITESPACE, 1) != null) { }
  }

  private def reset(): Unit = {
    scanner.reset()
    //scanner.useDelimiter(JsonReader.DELIMITER)
  }
}

object JsonReader {
  //val STRING = Pattern.compile("\"[a-z]*\"")
  val STRING = Pattern.compile("\"(((?=\\\\)\\\\([\"\\\\/bfnrt]|u[0-9a-fA-F]{4}))|[^\"\\\\\\x00-\\x1F\\x7F]+)*\"")
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
  val COMMA = Pattern.compile(",")
  val NULL = Pattern.compile("null")
  val WHITESPACE = Pattern.compile("[\\x20\\x09\\x0A\\x0D]")

  def quote(javaString: String): String = "\"" + javaString + "\""
  def unquote(jsonString: String): String = jsonString.substring(1, jsonString.length - 1)
}

object JsonReaderApp extends App {
  println("Hi Bro!")
  println()
  println(new JsonReader("\"lol\" hohoho").readJsonString())
  println(new JsonReader("     \"lol\\\"lol\" hohoho").readJsonString())
}