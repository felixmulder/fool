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

/**
 * The `List` interface is the shared interface between a `NonEmptyList` and an
 * empty list `Nil`
 *
 * The `List.apply` function creates a lazy list `::`. All operations on said
 * list are evaluated in a truly lazy manner. Only the head will be evaluated
 * on prints.
 */
sealed trait List[+A] extends Seq[A] { self =>
  def isEmpty: Boolean

  def ::[B >: A](x: B): List[B] =
    collection.::(() => x, () => this)

  override def equals(other: Any): Boolean =
    (self, other) match {
      case (ss: List[_], xs: List[_]) => ss equalMembers xs
      case _ => false
    }

  override def map[B](f: A => B): List[B] = Nil

  override def foldRight[B](start: => B)(f: (A, => B) => B): B = start
}

object List {
  def apply[A](args: A*): List[A] =
    if (!args.isEmpty) args.head :: apply(args.tail: _*)
    else Nil

  def empty[B]: List[B] = Nil
}

/**
 * The `NonEmptyList` implements equality for its subtypes
 */
sealed trait NonEmptyList[+A] extends List[A] with NonEmptySeq[A] { self =>
  override def equalMembers[B >: A](other: Seq[B]): Boolean = (self, other) match {
    case (xs: NonEmptyList[_], ys: NonEmptyList[_]) =>
      (xs.head == ys.head) && (xs.tail equalMembers ys.tail)
    case _ => false
  }
}

final case class ::[+A](hd: () => A, tl: () => List[A]) extends NonEmptyList[A] {
  lazy val head = hd()
  lazy val tail = tl()

  override def toString: String = s"List($head, ?)"

  override def map[B](f: A => B): List[B] =
    foldRight(List.empty[B]) { (hd,tl) => collection.::(() => f(hd), () => tl) }

  override def foldRight[B](start: => B)(f: (A, => B) => B): B =
    this match {
      case hd :: tl => f(hd(), tl().foldRight(start)(f))
      case _ => start
    }
}

object :: {
  def unapply[A](xs: List[A]) = xs match {
    case ps: NonEmptyList[A] => Some((ps.head, ps.tail))
    case _ => None
  }
}

final case object Nil extends List[Nothing] {
  def isEmpty = true

  override def equalMembers[B >: Nothing](other: Seq[B]) = other eq Nil
}
