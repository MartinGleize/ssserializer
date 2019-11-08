package ssserializer.deserializers.json.parsing

import java.io.{Closeable, Reader, StringReader}
import java.util.Scanner
import java.util.regex.Pattern

import ssserializer.deserializers.json.parsing
import ssserializer.util.RegexUtils

/**
 * Provides facilities to read JSON values in any Reader (often with an underlying InputStream).
 * Uses Scanner so Scanner-related restrictions apply (for example no two JsonReader reading on the same stream,
 * Scanner consumes the underlying stream so the behavior of a second Scanner reading is undefined).
 *
 * @param reader The original base reader accessing the JSON input source
 */
class ScannerJsonReader(override val reader: Reader) extends JsonReader {

  def this(s: String) { this(new StringReader(s)) }

  val scanner: Scanner = {
    val scanner = new Scanner(reader)
    //scanner.useDelimiter(JsonReader.DELIMITER)
    scanner
  }

  /** Read a JSON string value */
  def readJsonString(): String = {
    val res = next(parsing.JsonReader.STRING)
    // unescape
    JsonUtil.unescape(res)
  }

  /** Read a JSON numeric value */
  def readJsonNumber(): String = {
    val res = next(parsing.JsonReader.NUMBER)
    res.toString
  }

  /** Read a JSON boolean value */
  def readJsonBoolean(): String = {
    next(parsing.JsonReader.BOOLEAN)
  }

  private def next(pattern: Pattern): String = {
    scanner.findWithinHorizon(pattern, 0)
  }

  /** Jumps to the pattern and consumes it (ignores tokens in-between) */
  def skipAfter(pattern: Pattern): Unit = {
    scanner.findWithinHorizon(pattern, 0)
  }

  /** Jumps to the pattern and consumes it (ignores tokens in-between) */
  def skipAfter(token: String): Unit = {
    skipAfter(RegexUtils.escapeRegex(token).r.pattern)
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

  override def tryToConsumeToken(token: String): Boolean = {
    tryToConsumeNextToken(RegexUtils.escapeRegex(token).r.pattern)
  }

  private def consumeNextWhitespaces(): Unit = {
    while (scanner.findWithinHorizon(parsing.JsonReader.WHITESPACE, 1) != null) { }
  }

  private def reset(): Unit = {
    scanner.reset()
    //scanner.useDelimiter(JsonReader.DELIMITER)
  }

  override def close(): Unit = scanner.close()
}

object JsonReaderApp extends App {
  println("Hi Bro!")
  println()
  println(new ScannerJsonReader("\"lol\" hohoho").readJsonString())
  println(new ScannerJsonReader("     \"lol\\\"lol\" hohoho").readJsonString())
}