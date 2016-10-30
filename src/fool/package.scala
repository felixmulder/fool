package object fool {
  /** Import basic types from scala/java */
  type Any           = scala.Any
  type AnyVal        = scala.AnyVal
  type Boolean       = scala.Boolean
  type Byte          = scala.Byte
  type Char          = scala.Char
  type Double        = scala.Double
  type Float         = scala.Float
  type Int           = scala.Int
  type Long          = scala.Long
  type Nothing       = scala.Nothing
  type Short         = scala.Short
  type String        = java.lang.String
  type Unit          = scala.Unit

  /** Import StringContext for `s"Interpolated $string"` */
  type StringContext = scala.StringContext
  val  StringContext = scala.StringContext

  /** Default imports */
  type List[+A] = fool.collection.List[A]
  val  List     = fool.collection.List
  val  Nil      = fool.collection.Nil
  type ::[+A]   = fool.collection.::[A]
  val  ::       = fool.collection.::

  /** Import println */
  def print(x: Any): Unit   = scala.Predef.print(x)
  def println(x: Any): Unit = scala.Predef.println(x)
  def println(): Unit       = scala.Predef.println()

  /** Identity function returns itself */
  def identity[A]: A => A = x => x

  /** When using viewbounds this will procure an implicit from the compiler */
  @inline def implicitly[T](implicit e: T) = e

  /* Annotations */
  type inline = scala.inline
  type switch = scala.annotation.switch
  type implicitNotFound = scala.annotation.implicitNotFound
  def ??? : Nothing = scala.Predef.???

  /** Exceptions */
  type Throwable = java.lang.Throwable
  type NoSuchElementException = java.util.NoSuchElementException

  /** Proofs */
  @implicitNotFound(msg = "Cannot prove that ${From} <:< ${To}.")
  sealed abstract class <:<[-From, +To] extends (From => To) with java.io.Serializable
  private[this] final val singleton_<:< = new <:<[Any,Any] { def apply(x: Any): Any = x }
  implicit def $conforms[A]: A <:< A = singleton_<:<.asInstanceOf[A <:< A]


  /** Any ops */
  implicit class FoolAny[A](val a: A) extends AnyVal {
    def ->[B](b: B): (A,B) = (a, b)
  }
}
