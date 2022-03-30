package u05lab.ex1

import org.junit.Test
import org.junit.Assert.*

class ListTest {

  val reference: List[Int] = List(1, 2, 3, 4)

  @Test
  def testZipRight(): Unit =
    assertEquals(List((1, 0), (2, 1), (3, 2), (4, 3)), reference.zipRight)

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

}
