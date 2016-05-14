package fool
package test

import org.scalatest._

class OptionSpec extends FlatSpec with Matchers {

  "An Option" should "create None from Option.apply(null)" in {
    Option(null) should be (None)
  }

  it should "have the correct values for `isEmpty`" in {
    Some(1).isDefined should be (true)
    None.isDefined    should be (false)
  }

  it should "handle different cases of `getOrElse`" in {
    (Some(1) getOrElse 2) should be (1)
    (None getOrElse 2)    should be (2)
  }

  it should "handle different cases of `map`" in {
    val some = Some(3).map(_ * 7).getOrElse(-1)
    val none = (None: Option[Int]).map(_ * 7).getOrElse(-1)

    some should be (21)
    none should be (-1)
  }

  it should "handle different cases fo `flatMap`" in {
    Some("identity").flatMap(x => Some(x)) should be (Some("identity"))

    (None: Option[String]).flatMap(x => Some(x)) should be (None)
  }

  it should "handle diferent cases of `fold`" in {
    val some = Some(3).fold(-1)(_ * 7)
    val none = (None: Option[Int]).fold(-1)(_ * 7)

    some should be (21)
    none should be (-1)
  }

  it should "handle different cases of `flatten`" in {
    Some(Some(1)).flatten should be (Some(1))
  }


  it should "handle `map2` correctly" in {
    val none1 = Option.map2(Some(1), (None: Option[Int])) { _ + _ }
    val none2 = Option.map2((None: Option[Int]), Some(2)) { _ + _ }
    val some  = Option.map2(Some(3), Some(7)) { _ + _ }

    none1 should be (None)
    none2 should be (None)
    some should be (Some(10))
  }

  it should "handle sequencing" in {
    val somes = Option.sequence(Some(1) :: Some(2) :: Some(3) :: Nil).getOrElse(Nil)
    val nones = Option.sequence(None :: None :: None :: Nil).getOrElse(Nil)
    val mixed = Option.sequence(Some(1) :: None :: Some(2):: None :: Nil).getOrElse(Nil)

    somes should be (1 :: 2 :: 3 :: Nil)
    nones should be (Nil)
    mixed should be (Nil)
  }

  it should "handle `traverse`" in {
    val somes = Some(1) :: Some(2) :: Some(3) :: Nil

    Option.traverse(somes)(x => x) should be (Option.sequence(somes))

    Option.traverse(somes)(s => s.map(_ + 2)) should be
    (Option.sequence(somes).map(_.map(_ + 2)))
  }


  "A Some" should "return an Option[A] from its apply method" in {
    import scala.reflect.ClassTag
    def tag[T](v: T)(implicit ev: ClassTag[T]) = ev.toString

    tag(Some(1)) should be (tag(Option(1)))
  }

  it should "be able to pattern match" in {
    Some(1) match {
      case None => fail("Shouldn't match None")
      case Some(1) => // correct!
      case _ => fail("Didn't match Some/None")
    }

    Option(1) match {
      case None => fail("Shouldn't match None")
      case Some(1) => // correct!
      case _ => fail("Didn't match Some/None")
    }

    (None: Option[Int]) match {
      case Some(1) => // correct!
      case None => // correct!
      case _ => fail("Didn't match Some/None")
    }
  }
}
