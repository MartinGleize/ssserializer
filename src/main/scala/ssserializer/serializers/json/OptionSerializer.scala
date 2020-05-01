package ssserializer.serializers.json
import java.io.BufferedWriter

class OptionSerializer extends ssserializer.serializers.generic.OptionSerializer[BufferedWriter] with NullHandlingSerializer[Option[_]] {

  /** Happens before the option is serialized */
  override def outputStart(w: Writer): Unit = {
    w.write("[")
  }

  /** Happens after the value of the option has been serialized */
  override def outputEnd(w: Writer): Unit = {
    w.write("]")
  }
}
