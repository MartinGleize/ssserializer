package ssserializer.deserializers.json

import ssserializer.deserializers.MasterDeserializer
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
    seqDetector -> defaults.seqDeserializer,
    mapDetector -> new MapDeserializer(),
    caseClassDetector -> new CaseClassDeserializer()
  )

}
