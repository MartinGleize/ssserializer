package ssserializer.deserializers.json.parsing

import java.io.{Closeable, Reader}
import java.util.regex.Pattern

/**
 * Provides facilities to read JSON values in any Reader (often with an underlying InputStream).
 */
trait JsonReader extends Closeable {

  /** The original base reader accessing the JSON input source */
  def reader: Reader

  /** Reads the name of a name/value pair (consumes the colon ':' character too) */
  def readJsonName(): String = {
    val res = readJsonString()
    skipAfter(JsonReader.COLON)
    res
  }

  /** Reads a JSON string value */
  def readJsonString(): String

  /** Reads a JSON numeric value */
  def readJsonNumber(): String

  /** Reads a JSON boolean value */
  def readJsonBoolean(): String

  /** Consumes exactly the next token (whitespaces not allowed) */
  def skipAfter(token: String): Unit

  /** Tries to consume exactly the next token (possibly preceded by whitespaces), returns true if it was successful */
  def tryToConsumeToken(token: String): Boolean
}

object JsonReader {
  //val STRING = Pattern.compile("\"[a-z]*\"")
  val STRING = Pattern.compile("\"(?:((?=\\\\)\\\\([\"\\\\/bfnrt]|u[0-9a-fA-F]{4}))|[^\"\\\\\\x00-\\x1F\\x7F]+)*\"")
  val STRING_START = Pattern.compile("\"")
  val STRING_END = Pattern.compile("(?<!\\\\)\"")
  //val NUMBER = Pattern.compile("[0-9]+")
  //val NUMBER = Pattern.compile("[0-9]+\\.[0-9]+")
  val NUMBER = Pattern.compile("-?(?:0|[1-9]\\d*)(?:\\.\\d+)?(?:[eE][+-]?\\d+)?")
  val BOOLEAN = Pattern.compile("(true)|(false)")
  val COLON = ":"
  val CURLY_OPEN = "{"
  val CURLY_CLOSE = "}"
  val BRACKET_OPEN = "["
  val BRACKET_CLOSE = "]"
  val COMMA = ","
  val NULL = "null"
  val WHITESPACE = Pattern.compile("[\\x20\\x09\\x0A\\x0D]")

  def quote(javaString: String): String = "\"" + javaString + "\""
  def unquote(jsonString: String): String = jsonString.substring(1, jsonString.length - 1)
}