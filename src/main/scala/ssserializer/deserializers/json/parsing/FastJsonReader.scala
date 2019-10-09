package ssserializer.deserializers.json.parsing
import java.io.{Reader, StringReader}
import java.util.regex.Pattern

import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.StringEscapeUtils

import scala.collection.mutable
/**
 * Direct implementation of a JSON reader that makes assumptions on the input: that it's well-formatted JSON, and that we
 * know what is the type of the next value to read (minor alternatives are possible, like null values)
 * Aimed to be very efficient but will break if the JSON is malformed in usually recoverable ways (for example an object
 * without closing curly brace)
 *
 * @param reader
 */
class FastJsonReader(override val reader: Reader) extends JsonReader {

  def this(s: String) { this(new StringReader(s)) }

  private val buffer: Array[Char] = new Array[Char](FastJsonReader.SIZE)
  private var start = 0
  private var length = 0
  private def text: CharSequence = new CharArraySequence(buffer, start, length)

  /** Reads a JSON string value */
  override def readJsonString(): String = {
    consumeWhitespaces()
    skipAfter(FastJsonReader.STRING_START)
    val res = readUntilSingleCharacterPattern(FastJsonReader.STRING_END)
    skipAfter(FastJsonReader.STRING_START) // consume the end "
    // unescape the result
    StringEscapeUtils.unescapeJson('"' + res + '"')
  }

  /** Reads a JSON numeric value */
  override def readJsonNumber(): String = {
    consumeWhitespaces()
    readAllOfSingleCharacterPattern(FastJsonReader.NUMBER)
  }

  /** Reads a JSON boolean value */
  override def readJsonBoolean(): String = {
    readNextOfTokens(FastJsonReader.TRUE_OR_FALSE)
  }

  /** Consumes exactly the next token (whitespaces not allowed) */
  override def skipAfter(token: String): Unit = {
    if (!tryToConsumeToken(token)) {
      throw new RuntimeException("Malformed JSON serialization, expected: " + token + " found " + text.charAt(0))
    }
  }

  /** Tries to consume exactly the next token (possibly preceded by whitespaces), returns true if it was successful */
  override def tryToConsumeToken(token: String): Boolean = {
    consumeWhitespaces()
    ensureAtLeast(token.length)
    if (StringUtils.startsWith(text, token)) {
      // we can indeed read this token
      advance(token.length)
      true
    } else false
  }

  private def readNextOfTokens(tokens: Seq[String]): String = {
    consumeWhitespaces()
    val res = tokens.collectFirst {
      case token if tryToConsumeToken(token) => token
    }
    res.getOrElse(throw new RuntimeException("Malformed JSON serialization, expected one of: " + tokens + " found " + text.charAt(0)))
  }

  private def consumeWhitespaces(): Unit = {
    skipAllOfSingleCharacterPattern(FastJsonReader.WHITESPACE)
  }

  private def readAllOfSingleCharacterPattern(regex: Pattern): String = {
    ensureNonEmpty()
    val sb = new mutable.StringBuilder()
    var lastCount = 0
    while ({lastCount = countLeadingSingleCharacterPattern(regex); lastCount} == length) {
      // read the string
      sb.appendAll(buffer.slice(start, start + length))
      // flush the buffer and reads more JSON
      readMore()
    }
    // finish the string
    sb.appendAll(buffer.slice(start, start + lastCount))
    // update the pointer
    advance(lastCount)
    // build the string
    sb.toString()
  }

  private def readUntilSingleCharacterPattern(regex: Pattern): String = {
    ensureNonEmpty()
    val sb = new mutable.StringBuilder()
    var lastCount = 0
    // we read as long as we don't find the pattern (and assume that we will find it before the end of the stream)
    while ({lastCount = countUntilSingleCharacterPattern(regex); lastCount} < 0) {
      // read the string
      sb.appendAll(buffer.slice(start, start + length))
      // flush the buffer and reads more JSON
      readMore()
    }
    // finish the string
    sb.appendAll(buffer.slice(start, start + lastCount))
    // update the pointer
    advance(lastCount)
    // build the string
    sb.toString()
  }

  private def skipAllOfSingleCharacterPattern(regex: Pattern): Unit = {
    ensureNonEmpty()
    var lastCount = 0
    while ({lastCount = countLeadingSingleCharacterPattern(regex); lastCount} == length) {
      // flush the buffer and reads more JSON
      readMore()
    }
    // lastCount is strictly less than length at this point TODO: or the text has ended
    advance(lastCount)
  }

  private def countLeadingSingleCharacterPattern(regex: Pattern): Int = {
    // assumes the text is not empty (length != 0)
    val lol = text
    val matcher = regex.matcher(text)
    // find the first occurrence of characters (there should be something)
    val found = matcher.find()
    if (found && matcher.start() == 0) {
      matcher.end()
    } else {
      throw new RuntimeException("Malformed JSON serialization, expected: " + regex + " found " + text.charAt(0))
    }
  }

  private def countUntilSingleCharacterPattern(regex: Pattern): Int = {
    // assumes the text is not empty (length != 0)
    val matcher = regex.matcher(text)
    // find the first occurence of characters (there can be nothing if we need to read more)
    val found = matcher.find()
    if (found) {
      matcher.start()
    } else {
      // not found
      -1
    }
  }

  private def advance(count: Int): Unit = {
    start += count
    length -= count
  }

  private def ensureAtLeast(minLength: Int): Unit = {
    if (length < minLength)
      readMore()
  }

  private def ensureNonEmpty(): Unit = ensureAtLeast(1)

  private def readMore(): Unit = {
    // don't check that reader can still read
    val amountRead = reader.read(buffer, 0, FastJsonReader.SIZE)
    start = 0
    length = amountRead
  }

  override def close(): Unit = reader.close()
}

object FastJsonReader {

  private val SIZE = 32768

  val WHITESPACE: Pattern = "\\s*".r.pattern
  val TRUE_OR_FALSE: Seq[String] = Seq("true", "false")
  val NUMBER: Pattern = "[\\-0-9.eE+]+".r.pattern
  val STRING_START: String = "\""
  val STRING_END: Pattern = "(?<!\\\\)\"".r.pattern
}
