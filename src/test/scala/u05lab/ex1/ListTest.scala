package u05lab.ex1

import org.junit.Test
import org.junit.Assert.*

class ListTest {

  val reference: List[Int] = List(1, 2, 3, 4)

  @Test
  def testZipRight(): Unit = {
    assertEquals(List((1, 0), (2, 1), (3, 2), (4, 3)), reference.zipRight)
  }

}
