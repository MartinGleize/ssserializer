package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer
import ssserializer.deserializers.json.parsing.JsonReader
import ssserializer.typing.detectors._
import ssserializer.typing.Detector

class JsonDeserializer extends MasterDeserializer[JsonReader] {

  override val deserializers: Seq[(Detector, ssserializer.deserializers.Deserializer[_, JsonReader])] = Seq(
    doubleDetector -> defaults.doubleDeserializer,
    longDetector -> defaults.longDeserializer,
    intDetector -> defaults.intDeserializer,
    booleanDetector -> defaults.booleanDeserializer,
    stringDetector -> defaults.stringDeserializer,
    optionDetector -> new OptionDeserializer(),
    arrayDetector -> defaults.arrayDeserializer,
    seqDetector[List[_]] -> new IterableOpsDeserializer[List, JsonReader](),
    seqDetector[Vector[_]] -> new IterableOpsDeserializer[Vector, JsonReader](),
    seqDetector[Seq[_]] -> defaults.seqDeserializer,
    setDetector -> defaults.setDeserializer,
    mapDetector -> new MapDeserializer(),
    caseClassDetector -> new CaseClassDeserializer(),
    tupleDetector -> new TupleDeserializer()
  )

}
