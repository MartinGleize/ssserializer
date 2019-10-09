package ssserializer.deserializers.json.parsing

import scala.collection.mutable.ArrayBuffer

/**
 * A char sequence backed-up by a char array.
 * Doesn't perform any safety check
 *
 * @param array arrays backing-up the sequence
 */
class CharArraySequence(val array: Array[Char], val offset: Int, override val length: Int) extends CharSequence {

  override def charAt(index: Int): Char = array(offset + index)

  override def subSequence(start: Int, end: Int): CharSequence = {
    new CharArraySequence(array, offset + start, offset + end)
  }

  override def toString: String = String.valueOf(array, offset, length)
}
