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

    list should    be (0 :: 1 :: 2 :: Nil)
    list should    be (0 :: 1 :: 2 :: Nil)
    list shouldNot be (1 :: 2 :: 3 :: Nil)
    list shouldNot be (Nil)
    list shouldNot be ("")
    index should   be (3)
  }

  it should "have the correct `toString`" in {
    (0 :: 1 :: Nil).toString  should be ("List(0, ?)")
    (Nil: List[Int]).toString should be ("Nil")
  }

  it should "have correct isEmpty" in {
    (0 :: 1 :: Nil).isEmpty  should be (false)
    (0 :: 1 :: Nil).nonEmpty should be (true)
    (Nil: List[Int]).isEmpty should be (true)
  }

  it should "support pattern matching" in {
    (0 :: 1 :: 2 :: Nil) match {
      case 0 :: 1 :: 2 :: Nil => // success!
      case _ => fail("Match error")
    }
  }

  it should "behave correctly when using List(...).apply(i)" in {
    val list = 1 :: 2 :: 3 :: 4 :: 5 :: 6 :: Nil

    list(0).getOrElse(-1)   should be (1)
    list(1).getOrElse(-1)   should be (2)
    list(2).getOrElse(-1)   should be (3)
    list(3).getOrElse(-1)   should be (4)
    list(4).getOrElse(-1)   should be (5)
    list(5).getOrElse(-1)   should be (6)
    list(6).getOrElse(-1)   should be (-1)
    list(-10).getOrElse(-1) should be (-1)

    (Nil: List[Int]).apply(1).getOrElse(-1) should be (-1)
  }

  it should "be able to handle `applyOrElse`" in {
    val list = 0 :: 1 :: 2 :: Nil

    list.applyOrElse(0)(-1) should be (0)
    list.applyOrElse(1)(-1) should be (1)
    list.applyOrElse(2)(-1) should be (2)
    list.applyOrElse(3)(-1) should be (-1)
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

  it should "be able to fold values" in {
    val list = 1 :: 2 :: 3 :: 4 :: Nil

    list.foldRight(0)(_ + _) should be (10)

    (Nil: List[Int]).foldRight(0)(_ * _) should be (0)
  }

  it should "be able to append another list" in {
    val xs = List(1, 2) ++ Nil
    val ys = List(1, 2) ++ List(3, 4)
    val zs = Nil        ++ List(1, 2)

    xs should be (List(1, 2))
    ys should be (List(1, 2, 3, 4))
    zs should be (List(1, 2))
    (Nil ++ Nil) should be (Nil)
  }

  it should "not evaluate when appending" in {
    var i = 0
    def incr = { () => i += 1; i }

    val list: List[Int] =
      collection.::(incr, () => collection.::(incr, () => Nil))

    i should be (0)                     // No increase when creating
    val concat = (list ++ List(3, 4))
    i should be (0)                     // No increase after appending list
    concat should be (List(1, 2, 3, 4))
    i should be (2)                     // When list is evaluated, the first
                                        // part should increase counter to 2
  }

  it should "be able to `flatMap` properly" in {
    val xss = List(List(1, 2), List(3, 4))
    (xss flatMap identity) should be (List(1, 2, 3, 4))
  }

  it should "not evaluate when using `flatMap`" in {
    var i = 0
    def incr = { () => i += 1; i }

    val xs =
      collection.::(incr, () => collection.::(incr, () => Nil))
    val ys =
      collection.::(incr, () => collection.::(incr, () => Nil))

    val listOfLists: List[List[Int]] = collection.::(() => xs, () => collection.::(() => ys, () => Nil))
    val flat: List[Int] = listOfLists flatMap identity

    i should be (0)
    flat should be (List(1, 2, 3, 4))
    i should be (4)
  }

  it should "be able to filter a list" in {
    val xs = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    xs.filter(_ > 5) should be (List(6, 7, 8, 9, 10))
  }

  it should "not evaluate list when filtering" in {
    var i = 0
    def incr = { () => i += 1; i }
    val list: List[Int] =
      collection.::(
        incr,
        () => collection.::(
          incr,
            () => collection.::(
              incr,
                () => Nil)))

    list.filter(_ > 1).map(_ * 2) should be (List(4, 6))
    i should be (3)
  }

  it should "be able to collect all" in {
    val xs = List(1, 2, 3)
    xs.collect { case x => x } should be (List(1, 2, 3))
  }

  it should "not collect elements larger than predicate" in {
    val xs = List(1, 2, 3, 4, 5)
    xs.collect { case x if x > 3 => x } should be (List(4, 5))
  }
}
