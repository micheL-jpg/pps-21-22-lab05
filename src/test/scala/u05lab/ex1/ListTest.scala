package u05lab.ex1

import org.junit.Test
import org.junit.Assert.*

class ListTest {

  val reference: List[Int] = List(1, 2, 3, 4)

  @Test
  def testZipRight(): Unit =
    assertEquals(List((1, 0), (2, 1), (3, 2), (4, 3)), reference.zipRight)
    assertEquals(List((1, 0), (2, 1), (3, 2), (4, 3)), reference.zipRightWithFold)
    assertEquals(List((1, 0), (2, 1), (3, 2), (4, 3)), reference.zipRightWithMap)

  @Test def testPartition(): Unit =
    assertEquals((List(1,2), List(3,4)), reference.partition(_ <= 2))

  @Test def testSpan(): Unit =
    assertEquals((List(1,2), List(3,4)), reference.span(_ < 3))

  @Test def testReduce(): Unit =
    assertThrows(classOf[UnsupportedOperationException], () => List.Nil[Int]().reduce(_ + _))
    assertEquals(10, reference.reduce(_ + _))

  @Test def testTakeRight(): Unit =
    assertEquals(List(3,4), reference.takeRight(2))
    assertEquals(List(3,4), reference.takeRightWithNoReverse(2))
    assertEquals(List(2,3,4), reference.takeRightWithNoReverse(3))
    assertEquals(List(3,4), reference.takeRightWithCollect(2))
    assertEquals(List(2,3,4), reference.takeRightWithCollect(3))

  @Test def testCollectIntToInt(): Unit =
    val f: PartialFunction[Int, Int] = {
      case d if d != 0 => 24 / d
    }
    assertEquals(List(24,12,8), List(0,1,2,3) collect f)

  @Test def testCollectIntToString(): Unit =
    val convert1to2: PartialFunction[Int, String] = new PartialFunction[Int, String] {
      val nums: Array[String] = Array("one", "two")
      def apply(i: Int): String = nums(i-1)
      def isDefinedAt(i: Int): Boolean = i > 0 && i < 3
    }
    assertEquals(List("one","two"), List(0,1,2,3) collect convert1to2)

}
