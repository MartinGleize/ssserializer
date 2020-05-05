package ssserializer.deserializers.json.compact

import java.io.Reader

import ssserializer.deserializers.json.compact.toplevel.TopLevelArrayDeserializer
import ssserializer.deserializers.json.parsing.JsonReader

import scala.collection.mutable

import scala.reflect.runtime.universe._

class JsonReaderWithMemory(val originalReader: JsonReader, val topLevelArrayDeserializer: TopLevelArrayDeserializer) extends JsonReader {

  val refs = new mutable.HashMap[Int, Any]()

  /** Allocate a memory spot for a future reference, the type is only used for debugging */
  def allocateIdForRef(t: Type): Int = {
    val id = refs.size
    refs(id) = IntAsRef(id, t)
    id
  }

  def updateRef(id: Int, ref: Any): Any = {
    refs(id) = ref
  }

  /*##### Delegate everything JSON-reading related to the base JsonReader */
  override def reader: Reader = originalReader.reader
  override def readJsonString(): String = originalReader.readJsonString()
  override def readJsonNumber(): String = originalReader.readJsonNumber()
  override def readJsonBoolean(): String = originalReader.readJsonBoolean()
  override def skipAfter(token: String): Unit = originalReader.skipAfter(token)
  override def tryToConsumeToken(token: String): Boolean = originalReader.tryToConsumeToken(token)
  override def close(): Unit = originalReader.close()
}
