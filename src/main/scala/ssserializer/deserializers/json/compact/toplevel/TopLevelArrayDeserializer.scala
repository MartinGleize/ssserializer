package ssserializer.deserializers.json.compact.toplevel

import ssserializer.deserializers.json.compact.{IntAsRef, JsonReaderWithMemory, RefParsingDeserializer}
import ssserializer.deserializers.json.defaults
import ssserializer.deserializers.json.parsing.JsonReader
import ssserializer.deserializers.{DeserializationException, Deserializer, MasterDeserializer}
import ssserializer.typing.Detector
import ssserializer.typing.detectors._

import scala.collection.mutable.ArrayBuffer
import scala.reflect.runtime.universe._

class TopLevelArrayDeserializer extends MasterDeserializer[JsonReaderWithMemory] {

  private val refParsingDeserializer = new RefParsingDeserializer()

  def deserializeRefDependencies(allDependencies: Seq[_], jsonReader: JsonReaderWithMemory): Seq[Any] = {
    val realArgs = new ArrayBuffer[Any]()
    for (dependency <- allDependencies) dependency match {
      case IntAsRef(id, t) =>
        // this is an int ref, we possibly have to read the next top level JSON array item
        val realArg = jsonReader.refs.getOrElseUpdate(id, deserialize(t, jsonReader))
        realArgs += realArg
      case _ => realArgs += dependency // this is a value type, added as is
    }
    realArgs
  }

  override def deserialize(t: Type, jsonReader: JsonReaderWithMemory,
                           parentDeserializer: MasterDeserializer[JsonReaderWithMemory] = refParsingDeserializer): Any = {
    // here we need to read either:
    // '[' when at the very beginning of the JSON array
    // ',' for any subsequent ref dependency
    // ']' if something wrong has happened (we shouldn't need to read more refs than needed)
    if (jsonReader.tryToConsumeToken(JsonReader.BRACKET_CLOSE)) {
      // there was a problem, we shouldn't have to read the end-of-array character (superfluous)
      throw new DeserializationException("Read ']', end of top-level JSON array instead", t)
    } else if (jsonReader.tryToConsumeToken(JsonReader.BRACKET_OPEN) || jsonReader.tryToConsumeToken(JsonReader.COMMA)) {
      // we allocate a spot in memory for the future object
      val memId = jsonReader.allocateIdForRef(t)
      // we read the next object normally (using a refParsingDeserializer for dependencies)
      val res = super.deserialize(t, jsonReader, parentDeserializer)
      // we put it into memory
      jsonReader.updateRef(memId, res)
      // return the resulting object
      res
    } else {
      // '[', ',', and ']' couldn't be read, which means that the JSON array is malformed
      throw new DeserializationException("Couldn't read { [ , ] } before item on the top-level JSON array", t)
    }
  }

  /** Pairs of a detector and the deserializer supporting the type it detects */
  override val deserializers: Seq[(Detector, Deserializer[_, JsonReaderWithMemory])] = Seq(
    doubleDetector -> defaults.doubleDeserializer,
    longDetector -> defaults.longDeserializer,
    intDetector -> defaults.intDeserializer,
    booleanDetector -> defaults.booleanDeserializer,
    stringDetector -> defaults.stringDeserializer,
    optionDetector -> new OptionDeserializer(),
    arrayDetector -> new IterableOpsDeserializer[Seq]().convertToDeserializer(_.toArray),
    seqDetector[List[_]] -> new IterableOpsDeserializer[List](),
    seqDetector[Vector[_]] -> new IterableOpsDeserializer[Vector](),
    seqDetector[Seq[_]] -> new IterableOpsDeserializer[Seq](),
    setDetector -> new IterableOpsDeserializer[Set](),
    mapDetector -> new MapDeserializer(),
    caseClassDetector -> new CaseClassDeserializer(),
    tupleDetector -> new TupleDeserializer()
  )

}
