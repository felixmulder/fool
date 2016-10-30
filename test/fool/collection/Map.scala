package fool
package collection
package test

import org.scalatest._

class MapSpec extends FlatSpec with Matchers {
  "A Map" should "be created properly with apply" in {
    val ms = Map("A" -> 1, "B" -> 2, "C" -> 3, "D" -> 4)

    ms("A") shouldEqual Some(1)
    ms("B") shouldEqual Some(2)
    ms("C") shouldEqual Some(3)
    ms("D") shouldEqual Some(4)

    ms.size shouldEqual (4)
  }

  it should "behave correctly for contains" in {
    val ms = Map("A" -> 1, "B" -> 2, "C" -> 3, "D" -> 4)

    ms.contains("A") shouldBe (true)
    ms.contains("X") shouldBe (false)
  }

  it should "behave correctly for map" in {
    val ms1 = Map("A" -> 1, "B" -> 2, "C" -> 3, "D" -> 4)
    val ms2 = Map("A" -> 2, "B" -> 4, "C" -> 6, "D" -> 8)

    ms1.map { case (k, v) => (k, v * 2) } shouldEqual (ms2)
  }

  it should "have no duplicates when creating using apply" in {
    val ms = Map("A" -> 1, "A" -> 2)

    ms("A") shouldEqual Some(1)
    ms.size shouldEqual (1)
  }
}
