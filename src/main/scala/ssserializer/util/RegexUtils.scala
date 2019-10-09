package ssserializer.util

object RegexUtils {
  def escapeRegex(stringForRegex: String): String = {
    stringForRegex.replaceAll("([\\\\+*?\\[\\](){}|.^$])", "\\\\$1")
  }
}
