package ssserializer.deserializers.json.parsing
import java.io.{Reader, StringReader}

import scala.collection.mutable

/**
 * Implementation of a JSON reader using assumptions on the input:
 * that it's well-formatted JSON,
 * and that we know what is the type of the next value to read (minor alternatives are possible, like null values).
 *
 * Designed to be very efficient but will break if the JSON is malformed in usually recoverable ways
 * (for example a JSON object without closing curly brace)
 *
 * @param reader the underlying reader (often an InputStreamReader) accessing the JSON
 */
class ReallyFastJsonReader(override val reader: Reader) extends JsonReader {

  def this(s: String) { this(new StringReader(s)) }

  private val buffer: Array[Char] = new Array[Char](ReallyFastJsonReader.SIZE)
  private var start = 0
  private var length = 0
  private def charAt(pos: Int): Char = buffer(start + pos)

  /** Reads a JSON string value */
  override def readJsonString(): String = {
    consumeWhitespaces()
    skipAfter(ReallyFastJsonReader.STRING_START)
    val res = readStringValue()
    skipAfter(ReallyFastJsonReader.STRING_START) // consume the end "
    // unescape the result
    '"' + JsonUtil.unescape(res) + '"'
  }

  /** Reads a JSON numeric value */
  override def readJsonNumber(): String = {
    consumeWhitespaces()
    readOneOrMoreSingleCharacterPattern(ReallyFastJsonReader.NUMBER)
  }

  /** Reads a JSON boolean value */
  override def readJsonBoolean(): String = {
    readNextOfTokens(FastJsonReader.TRUE_OR_FALSE)
  }

  /** Consumes exactly the next token (whitespaces not allowed) */
  override def skipAfter(token: String): Unit = {
    if (!tryToConsumeToken(token)) {
      throw new RuntimeException("Malformed JSON serialization, expected: " + token + " found " + charAt(0))
    }
  }

  /** Tries to consume exactly the next token (possibly preceded by whitespaces), returns true if it was successful */
  override def tryToConsumeToken(token: String): Boolean = {
    consumeWhitespaces()
    ensureAtLeast(token.length)
    val textStartsWithToken = {
      var res = true
      var i = 0
      while (i < token.length) {
        res &&= charAt(i) == token(i)
        i += 1
      }
      res
    }
    if (textStartsWithToken) {
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
    res.getOrElse(throw new RuntimeException("Malformed JSON serialization, expected one of: " + tokens + " found " + charAt(0)))
  }

  private def consumeWhitespaces(): Unit = {
    skipAllOfSingleCharacterPattern(ReallyFastJsonReader.WHITESPACE)
  }

  private def readStringValue(): String = {
    val sb = new mutable.StringBuilder()
    // we read as long as we don't find the pattern (and assume that we will find it before the end of the stream)
    do {
      ensureNonEmpty()
      // count characters until we encounter the regex (if not found, it returns 'length' because the count hasn't stopped)
      val lastCount = {
        var i = 0
        while (i < length && (charAt(i) != '"' || (i != 0 && charAt(i - 1) == '\\' ))) {
          i += 1
        }
        i
      }
      // read the string
      sb.appendAll(buffer.slice(start, start + lastCount))
      // update the buffer state
      advance(lastCount)
    }
    while (length == 0) // if we exhaust the buffer without finding the regex, try again with a fresh read, we will find it
    // build the string
    sb.toString()
  }

  private def readOneOrMoreSingleCharacterPattern(possibleChars: Array[Char], ignoreReads: Boolean = false): String = {
    val sb = new mutable.StringBuilder()
    var lastCount = 0
    // if we exhaust the buffer, we fill it back up again and continue reading
    while (ensureNonEmpty() && {lastCount = countLeadingSingleCharacterPattern(possibleChars); lastCount} > 0) {
      if (!ignoreReads) {
        // read the string
        sb.appendAll(buffer.slice(start, start + lastCount))
      }
      // update the buffer state
      advance(lastCount)
    }
    // if it's the end of the stream, or some invalid character has been found:
    // build the string
    sb.toString()
  }

  private def skipAllOfSingleCharacterPattern(possibleChars: Array[Char]): Unit = {
    readOneOrMoreSingleCharacterPattern(possibleChars, ignoreReads = true)
  }

  private def countLeadingSingleCharacterPattern(possibleChars: Array[Char]): Int = {
    // assumes the text is not empty (length != 0)
    var i = 0
    while (i < length && contains(charAt(i), possibleChars)) {
      i += 1
    }
    i
  }

  private def advance(count: Int): Unit = {
    start += count
    length -= count
  }

  private def ensureAtLeast(minLength: Int): Boolean = {
    if (length < minLength) readMore() > 0 else true
  }

  private def ensureNonEmpty(): Boolean = ensureAtLeast(1)

  private def contains(c: Char, possibleChars: Array[Char]): Boolean = {
    var i = 0
    while (i < possibleChars.length) {
      if (c == possibleChars(i))
        return true
      i += 1
    }
    false
  }

  /** For debugging */
  private def canReadMore: Boolean = length >= 0

  private def readMore(): Int = {
    // copy any remaining input (shouldn't ever be larger than 5 characters anyway: the length of "false")
    System.arraycopy(buffer, start, buffer, 0, length)
    start = 0
    // this call will return -1 if we reach the end of the JSON, we turn it into 0 instead
    val read = reader.read(buffer, length, ReallyFastJsonReader.SIZE - length)
    val amountRead = if (read >= 0) read else 0
    length += amountRead
    length
  }

  override def close(): Unit = reader.close()
}

object ReallyFastJsonReader {

  private val SIZE = 32768

  val WHITESPACE: Array[Char] = " \t\n\r".toCharArray
  val TRUE_OR_FALSE: Seq[String] = Seq("true", "false")
  val NUMBER: Array[Char] = "-0123456789.eE+".toCharArray
  val STRING_START: String = "\""
}
