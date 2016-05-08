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
}
