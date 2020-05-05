package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer
import ssserializer.deserializers.json.parsing.JsonReader

import scala.reflect.runtime.universe

trait StringParseDeserializer[T, JsonInput <: JsonReader] extends NullHandlingDeserializer[T, JsonInput] {

  override def deserializeNonNull(t: universe.Type, src: JsonInput, parentDeserializer: MasterDeserializer[JsonInput] = null): T = {
    val res = read(src)
    parse(res, t)
  }

  def read(jsonReader: JsonInput): String

  def parse(stringResult: String): T

  /** In most case you do not need the type to parse the string, but some classes might need it and choose to override this */
  def parse(stringResult: String, t: universe.Type): T = {
    parse(stringResult)
  }
}
