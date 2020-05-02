package ssserializer.serializers.json.compact

import org.scalatest.Assertion
import ssserializer.UnitSpec
import ssserializer.serializers.json.compact.traversal.{CompactJsonMemory, RefTraversalSerializer}

import scala.reflect.runtime.universe._

class RefTraversalSerializerTest extends UnitSpec {

  type OptionListOfStrings = List[Option[String]]
  type OptionListOfInts = List[Option[Int]]

  val testCasesString: Seq[TraversalTestCase[OptionListOfStrings]] = List(
    TraversalTestCase(null, Seq(null)),
    TraversalTestCase(List(), Seq(List())),
    TraversalTestCase(List(null), Seq(List(null), null)),
    TraversalTestCase(List[Option[String]](Some(null)), Seq(List(Some(null)), Some(null), null))

  )
  val testCasesInt: Seq[TraversalTestCase[OptionListOfInts]] = List()

  "The RefTraversalSerializer" should "store in memory the reference tree in depth-first order correctly" in {
    testCasesString.foreach(test(_))
    testCasesInt.foreach(test(_))
  }

  case class TraversalTestCase[T : TypeTag](data: T, expectedRefList: Seq[Any])

  def test[T : TypeTag](testCase: TraversalTestCase[T]): Assertion = {
    traversalSerialize(testCase.data) should be (testCase.expectedRefList)
  }

  def traversalSerialize[T : TypeTag](data: T): Seq[Any] = {
    val refTraversalSerializer = new RefTraversalSerializer()
    val memory = new CompactJsonMemory()
    // execute the first pass
    refTraversalSerializer.serialize(data, memory)
    // memory should now contain the list of references to serialize in the second pass
    // return all these references (stripped of their types)
    memory.refs.map(_._1)
  }
}
