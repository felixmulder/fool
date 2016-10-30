/*      _______  _______  _______  _                                          *\
**     (  ____ \(  ___  )(  ___  )( \          Functional                     **
**     | (    \/| (   ) || (   ) || (          Object-Oriented                **
**     | (__    | |   | || |   | || |          Library                        **
**     |  __)   | |   | || |   | || |                                         **
**     | (      | |   | || |   | || |          A Scala Standard               **
**     | )      | (___) || (___) || (____/\    Library Replacement            **
**     |/       (_______)(_______)(_______/                                   **
\*                                                                            */

package fool

import scala.{ Option => StdOption }

/**
 * Implements the Option interface equivalent to `scala.Option` in every way
 * except for the `get` method. This method is `protected[fool]` and as such,
 * no access to it is allowed outside of the implementation.
 */
sealed trait Option[+A] {
  /** `get` in this library is private to the implementing library */
  protected[fool] def get: A

  def isEmpty: Boolean

  def isDefined: Boolean = !isEmpty

  @inline final def getOrElse[B >: A](default: => B): B =
    if (isEmpty) default else get

  @inline final def map[B](f: A => B): Option[B] =
    if (isEmpty) None else Some(f(get))

  @inline def flatMap[B](f: A => Option[B]): Option[B] =
    if(isEmpty) None else f(get)

  @inline final def fold[B](onEmpty: => B)(f: A => B): B =
    if(isEmpty) onEmpty else f(get)

  def flatten[B](implicit ev: A <:< Option[B]): Option[B] =
    if (isEmpty) None else ev(get)
}

object Option {

  def apply[A](a: A): Option[A] = if (a != null) new Some(a) else None

  def map2[A,B,C](a: Option[A], b: Option[B])(f: (A, B) => C): Option[C] =
    a flatMap (aa => b map (bb => f(aa, bb)))

  def sequence[A](list: List[Option[A]]): Option[List[A]] =
    traverse(list)(identity)

  def traverse[A, B](list: List[A])(f: A => Option[B]): Option[List[B]] =
    list.foldRight[Option[List[B]]](Some(Nil)){ (elem, acc) =>
      map2(f(elem), acc){ (h, t) =>
        collection.::(() => h, () => t)
      }
    }

  def fromScalaOption[A](opt: StdOption[A]): Option[A] =
    if (opt.isDefined) Option.apply(opt.get)
    else None
}

final class Some[+A](a: A) extends Option[A] {
  override def get = a
  override def isEmpty = false

  override def equals(other: Any) = other match {
    case Some(x) => a == x
    case _ => false
  }

  override def hashCode = a.hashCode
  override def toString = s"Some($a)"
}

object Some {
  def apply[A](a: A): Option[A] = Option.apply(a)
  def unapply[A](opt: Some[A])  = opt
}

final case object None extends Option[Nothing] {
  override def get = throw new NoSuchElementException("None.get")
  override def isEmpty = true
}
