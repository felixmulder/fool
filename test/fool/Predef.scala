package fool
package test

import org.scalatest._

class PredefSpec extends FlatSpec with Matchers {
  "The predef" should "provide print/println" in {
    val baos = new java.io.ByteArrayOutputStream
    val ps = new java.io.PrintStream(baos)
    scala.Console.withOut(ps) {
      println("hello")
      println()
      print("wat")
    }

    baos.toString("utf8") should be ("hello\n\nwat")
  }

  it should "throw exception on ???" in {
    try {
      ???
      fail("did not throw exception for ???")
    } catch { case e: Throwable => }
  }
}
