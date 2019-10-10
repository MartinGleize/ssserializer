package ssserializer.deserializers.json.parsing
import java.io.{Reader, StringReader}
import java.util.regex.Pattern

import org.apache.commons.lang3.StringUtils
import org.apache.commons.text.StringEscapeUtils

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
    '"' + StringEscapeUtils.unescapeJson(res) + '"'
  }

  /** Reads a JSON numeric value */
  override def readJsonNumber(): String = {
    consumeWhitespaces()
    readOneOrMoreSingleCharacterPattern(FastJsonReader.NUMBER)
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

  private def readUntilSingleCharacterPattern(regex: Pattern): String = {
    val sb = new mutable.StringBuilder()
    // we read as long as we don't find the pattern (and assume that we will find it before the end of the stream)
    do {
      ensureNonEmpty()
      // count characters until we encounter the regex (if not found, it returns 'length' because the count hasn't stopped)
      val lastCount = countUntilSingleCharacterPattern(regex)
      // read the string
      sb.appendAll(buffer.slice(start, start + lastCount))
      // update the buffer state
      advance(lastCount)
    }
    while (length == 0) // if we exhaust the buffer without finding the regex, try again with a fresh read, we will find it
    // build the string
    sb.toString()
  }

  private def readOneOrMoreSingleCharacterPattern(regex: Pattern): String = {
    val sb = new mutable.StringBuilder()
    var lastCount = 0
    // if we exhaust the buffer, we fill it back up again and continue reading
    while (ensureNonEmpty() && {lastCount = countLeadingSingleCharacterPattern(regex); lastCount} > 0) {
      // read the string
      sb.appendAll(buffer.slice(start, start + lastCount))
      // update the buffer state
      advance(lastCount)
    }
    // if it's the end of the stream, or some invalid character has been found:
    // build the string
    sb.toString()
  }

  private def skipAllOfSingleCharacterPattern(regex: Pattern): Unit = {
    ensureNonEmpty()
    do {
      // consume every character matching the regex
      val lastCount = countLeadingSingleCharacterPattern(regex)
      // update the buffer state
      advance(lastCount)
    }
    while (length == 0 && readMore() > 0) // we continue as long as we exhaust the buffer and the JSON hasn't ended
  }

  private def countLeadingSingleCharacterPattern(regex: Pattern): Int = {
    // assumes the text is not empty (length != 0)
    val matcher = regex.matcher(text)
    // find the first occurrence of characters (there should be something)
    val found = matcher.find()
    if (found && matcher.start() == 0) {
      matcher.end()
    } else {
      // either we didn't find anything or we found it by skipping other characters
      0
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
      length
    }
  }

  private def advance(count: Int): Unit = {
    start += count
    length -= count
  }

  private def ensureAtLeast(minLength: Int): Boolean = {
    if (length < minLength) readMore() > 0 else true
  }

  private def ensureNonEmpty(): Boolean = ensureAtLeast(1)

  /** For debugging */
  private def canReadMore: Boolean = length >= 0

  private def readMore(): Int = {
    // copy any remaining input (shouldn't ever be larger than 5 characters anyway: the length of "false")
    System.arraycopy(buffer, start, buffer, 0, length)
    start = 0
    // this call will return -1 if we reach the end of the JSON, we turn it into 0 instead
    val read = reader.read(buffer, length, FastJsonReader.SIZE - length)
    val amountRead = if (read >= 0) read else 0
    length += amountRead
    length
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
