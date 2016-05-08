package object fool {
  /** Import basic types from scala/java */
  type Any           = scala.Any
  type AnyVal        = scala.AnyVal
  type Boolean       = scala.Boolean
  type Double        = scala.Double
  type Float         = scala.Float
  type Int           = scala.Int
  type Long          = scala.Long
  type Nothing       = scala.Nothing
  type String        = java.lang.String
  type Unit          = scala.Unit

  /** Import StringContext for `s"Interpolated $string"` */
  type StringContext = scala.StringContext
  val  StringContext = scala.StringContext

  /** Import println */
  def print(x: Any): Unit   = scala.Predef.print(x)
  def println(x: Any): Unit = scala.Predef.println(x)
  def println(): Unit       = scala.Predef.println()

  /* Annotations */
  type inline = scala.inline
  type switch = scala.annotation.switch
  type implicitNotFound = scala.annotation.implicitNotFound
  def ??? : Nothing = scala.Predef.???

  /** Exceptions */
  type NoSuchElementException = java.util.NoSuchElementException

  /** Proofs */
  @implicitNotFound(msg = "Cannot prove that ${From} <:< ${To}.")
  sealed abstract class <:<[-From, +To] extends (From => To) with java.io.Serializable
  private[this] final val singleton_<:< = new <:<[Any,Any] { def apply(x: Any): Any = x }
  implicit def $conforms[A]: A <:< A = singleton_<:<.asInstanceOf[A <:< A]
}
