package ssserializer.serializers.json

import java.io.BufferedWriter

import scala.reflect.runtime.universe._

trait IteratorSerializer[S] extends ssserializer.serializers.generic.IteratorSerializer[S, BufferedWriter]
  with NullHandlingSerializer[S] {

  override def outputStart(w: BufferedWriter): Unit = {
    w.write("[")
  }

  override def outputEnd(w: BufferedWriter): Unit = {
    w.write("]")
  }

  override def outputAfterElement(element: Any, elementType: Type, w: Writer, hasNext: Boolean): Unit = {
    if (hasNext) {
      w.write(",")
    }
  }

}
