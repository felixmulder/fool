package fool
package collection
package test

import org.scalatest._

class ListSpec extends FlatSpec with Matchers {
  "A List" should "be created properly with apply" in {
    val xs: fool.collection.List[Int] = List(1, 2, 3)

    xs should be (1 :: 2 :: 3 :: Nil)
    List() should be (Nil)
  }

  it should "only be evaluated once when traversed" in {
    var index = 0
    def incr = { () =>
      val old = index
      index += 1
      old
    }

    val list: List[Int] =
      collection.::(
        incr,
        () => collection.::(
          incr,
            () => collection.::(
              incr,
                () => Nil)))

    list should be (0 :: 1 :: 2 :: Nil)
    list should be (0 :: 1 :: 2 :: Nil)
    index should be (3)
  }

  it should "support pattern matching" in {
    (0 :: 1 :: 2 :: Nil) match {
      case 0 :: 1 :: 2 :: Nil => // success!
      case _ => fail("Match error")
    }
  }

  it should "behave correctly when using List(...).apply(i)" in {
    val list = 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil

    list(0).getOrElse(-1) should be (1)
    list(1).getOrElse(-1) should be (2)
    list(2).getOrElse(-1) should be (3)
    list(3).getOrElse(-1) should be (4)
    list(4).getOrElse(-1) should be (5)
    list(5).getOrElse(-1) should be (6)
    list(6).getOrElse(-1) should be (-1)
  }

  it should "handle `map` correctly" in {
    val list       = 1 :: 2 :: 3 :: 4 :: 5  :: 6  :: Nil
    val listTimes2 = 2 :: 4 :: 6 :: 8 :: 10 :: 12 :: Nil

    list.map(_ * 2) should be (listTimes2)
    (Nil: List[Int]).map(_ * 2) should be (Nil)
  }

  it should "only traverse once for multiple `map` calls" in {
    var index = 0
    def incr = { () =>
      val old = index
      index += 1
      old
    }

    val list: List[Int] =
      collection.::(
        incr,
        () => collection.::(
          incr,
            () => collection.::(
              incr,
                () => Nil)))

    list.map(x => x).map(_ * 2).map(_ / 2) should be (0 :: 1 :: 2 :: Nil)
    index should be (3)
  }
}
