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
package collection

import scala.annotation.tailrec
import scala.collection.immutable.{ List => StdList, Nil => StdNil }

/**
 * The `List` interface is the shared interface between a `NonEmptyList` and an
 * empty list `Nil`
 *
 * The `List.apply` function creates a lazy list `::`. All operations on said
 * list are evaluated in a truly lazy manner. Only the head will be evaluated
 * on prints.
 */
sealed trait List[+A] { self =>
  def isEmpty: Boolean

  def nonEmpty: Boolean = !isEmpty

  def ++[B >: A](other: => List[B]): List[B]

  def apply(index: Int): Option[A]

  @inline def applyOrElse[B >: A](index: Int)(default: => B): B =
    apply(index).getOrElse(default)

  def collect[B](pf: scala.PartialFunction[A, B]) =
    foldRight(List.empty[B])((elem, acc) => if (pf.isDefinedAt(elem)) (pf(elem) :: acc) else acc)

  @tailrec final def drop(nbr: Int): List[A] = this match {
    case Nil => Nil
    case list if nbr < 0 => list
    case list @ (x :: xs) => if (nbr == 0) list else xs.drop(nbr - 1)
  }

  @tailrec final def dropWhile(p: A => Boolean): List[A] = this match {
    case Nil => Nil
    case list @ (x :: xs) => if (!p(x)) list else xs.dropWhile(p)
  }

  def equalMembers[B >: A](other: List[B]): Boolean

  def find(p: A => Boolean): Option[A] =
    this match {
      case Nil => None
      case x :: xs => if (p(x)) Some(x) else xs.find(p)
    }

  @inline def filter(f: A => Boolean): List[A] =
    foldRight(List.empty[A]) { (elem, acc) =>
      if (f(elem)) collection.::(() => elem, () => acc)
      else acc
    }

  @inline def filterNot(f: A => Boolean): List[A] =
    filter(a => !f(a))

  @tailrec final def forall(f: A => Boolean): Boolean =
    this match {
      case Nil => true
      case x :: xs => f(x) && xs.forall(f)
    }

  def map[B](f: A => B): List[B] =
    foldRight(List.empty[B]) { (hd,tl) => collection.::(() => f(hd), () => tl) }

  final def foldRight[B](start: => B)(f: (A, => B) => B): B =
    this match {
      case Nil => start
      case hd :: tl => f(hd, tl.foldRight(start)(f))
    }

  @tailrec final def foldLeft[B](start: => B)(f: (=> B, A) => B): B =
    this match {
      case Nil => start
      case hd :: tl => tl.foldLeft(f(start, hd))(f)
    }

  def flatMap[B](f: A => List[B]): List[B] =
    foldRight(List.empty[B])((elem, acc) => f(elem) ++ acc)

  def ::[B >: A](x: => B): List[B] =
    collection.::(() => x, () => this)

  override def equals(other: Any): Boolean =
    (self, other) match {
      case (ss: List[_], xs: List[_]) => ss equalMembers xs
      case _ => false
    }

  final def toScalaList: StdList[A] = {
    var xs: List[A]     = this
    var sxs: StdList[A] = StdNil

    while (!xs.isEmpty) {
      val head = xs.asInstanceOf[NonEmptyList[A]].head
      xs = xs.asInstanceOf[NonEmptyList[A]].tail
      sxs = sxs ++ StdList(head)
    }

    sxs
  }
}

object List {
  def apply[A](args: A*): List[A] =
    if (!args.isEmpty) args.head :: apply(args.tail: _*)
    else Nil

  def empty[B]: List[B] = Nil

  def unapplySeq[A](xs: List[A]) = Option(xs.toScalaList)

  def fromScalaList[A](xs: StdList[A]): List[A] = xs match {
    case StdList(x, xs @ _*) => collection.::(() => x, () => fromScalaList(xs.toList))
    case StdNil =>  Nil
  }
}

/**
 * The `NonEmptyList` implements equality for its subtypes
 */
sealed trait NonEmptyList[+A] extends List[A] { self =>
  def isEmpty = false

  def head: A
  def tail: List[A]

  def apply(index: Int): Option[A] =
    if (index > 0) (tail: @switch) match {
      case Nil => None
      case xs: NonEmptyList[_] => xs.apply(index - 1)
    }
    else if (index < 0) None
    else Some(head)

  def equalMembers[B >: A](other: List[B]): Boolean = (self, other) match {
    case (xs: NonEmptyList[_], ys: NonEmptyList[_]) =>
      (xs.head == ys.head) && (xs.tail equalMembers ys.tail)
    case _ => false
  }
}

final case class ::[+A] private (val hd: () => A, val tl: () => List[A]) extends NonEmptyList[A] {
  lazy val head = hd()
  lazy val tail = tl()

  def ++[B >: A](other: => List[B]): List[B] =
    collection.::(() => head, () => { tail ++ other })

  override def toString: String = s"List($head, ?)"
}

object :: {
  def unapply[A](xs: List[A]) = xs match {
    case ps: NonEmptyList[A] => Some((ps.head, ps.tail))
    case _ => None
  }
}

final case object Nil extends List[Nothing] {
  def isEmpty = true

  def ++[B >: Nothing](other: => List[B]): List[B] = other

  def equalMembers[B >: Nothing](other: List[B]) = other eq Nil

  def apply(index: Int) = None

  override def toString = "Nil"
}
