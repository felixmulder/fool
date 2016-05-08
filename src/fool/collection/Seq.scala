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

trait Seq[+A] { self =>
  def isEmpty: Boolean

  def nonEmpty: Boolean = !isEmpty

  def equalMembers[B >: A](other: Seq[B]): Boolean

  def apply(index: Int): Option[A] = None

  def map[B](f: A => B): Seq[B] = Nil
}

trait NonEmptySeq[+A] extends Seq[A] {
  def isEmpty = false

  def head: A
  def tail: List[A]

  override def apply(index: Int): Option[A] =
    if (index > 0) (tail: @switch) match {
      case Nil => None
      case xs: NonEmptyList[_] => xs.apply(index - 1)
    }
    else if (index < 0) None
    else Some(head)
}
