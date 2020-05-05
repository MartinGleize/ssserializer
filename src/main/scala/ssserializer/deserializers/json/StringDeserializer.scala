package ssserializer.deserializers.json

import ssserializer.deserializers.json.parsing.JsonReader

class StringDeserializer[JsonInput <: JsonReader] extends StringParseDeserializer[String, JsonInput] {

  override def read(jsonReader: JsonInput): String = {
    // return the next Json String value (with enclosing ", but unescaped)
    jsonReader.readJsonString()
  }

  override def parse(stringResult: String): String = {
    // this is a non-null string, should start and end with "
    val res = stringResult stripPrefix "\"" stripSuffix "\""
    // [don't] unescape, this is actually done in JsonReader.readJsonString
    //StringEscapeUtils.unescapeJson(res)
    res
  }
}
