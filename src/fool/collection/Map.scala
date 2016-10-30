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

import scala.Ordering

sealed trait Map[K,+V] { self =>
  def isEmpty: Boolean

  def apply(k: K): Option[V]

  def contains(k: K): Boolean =
    apply(k).isDefined

  def equalMembers[A >: V](other: Map[K, A]): Boolean = (self, other) match {
    case (_: EmptyMap[_], _) => other.isEmpty
    case (self: NonEmptyMap[K, V], other: NonEmptyMap[K, A]) =>
      self.key == other.key && self.value == other.value &&
      self.left == other.left && self.right == other.right
    case _ => false
  }

  def map[B: Ordering, C](f: (K, V) => (B, C)): Map[B, C]

  def size: Int

  override def equals(other: Any): Boolean =
    other match {
      case m2: Map[K, _] => self equalMembers m2
      case _ => false
    }
}

final case class EmptyMap[K]() extends Map[K, Nothing] {
  def isEmpty = true
  def apply(k: K) = None
  def map[B: Ordering, C](f: (K, Nothing) => (B, C)) = Map.empty
  val size = 0
}

final case class NonEmptyMap[K: Ordering,+V] private (
  val k: () => K,
  val v: () => V,
  val l: () => Map[K,V],
  val r: () => Map[K,V]
) extends Map[K,V] {
  lazy val key = k()
  lazy val value = v()
  lazy val left = l()
  lazy val right = r()

  def isEmpty = false

  def apply(k: K) = {
    val ord = implicitly[Ordering[K]].compare(k, key)
    if (ord == 0) Some(value)
    else if (ord < 0) right.apply(k)
    else /* if (ord > 0) */ left.apply(k)
  }

  def map[B: Ordering, C](f: (K, V) => (B, C)): Map[B, C] = {
    lazy val (mk, mv) = f(key, value)
    NonEmptyMap(() => mk, () => mv, () => left.map(f), () => right.map(f))
  }

  lazy val size =
    1 + left.size + right.size
}

object Map {
  import scala.collection.immutable.Vector

  def apply[K: Ordering, V](elems: (K, V)*): Map[K,V] = apply {
    // This solution be very ugly - FIXME
    elems.sortBy(_._1).foldLeft(Vector.empty[(K,V)]) { case (acc, (k, v)) =>
      if (acc.find(_._1 == k).isDefined) acc
      else acc :+ (k,v)
    }
  }

  private def apply[K: Ordering, V](vec: Vector[(K,V)]): Map[K, V] =
    if (vec.isEmpty) empty
    else if (vec.length == 1) {
      lazy val Vector((k, v)) = vec
      lazy val emp = () => EmptyMap[K]()
      NonEmptyMap(() => k, () => v, emp, emp)
    }
    else {
      val mid = vec.length / 2
      lazy val (left0, right) = vec.splitAt(mid)
      lazy val (left, Vector((k, v))) = left0.splitAt(mid - 1)

      NonEmptyMap(
        () => k, () => v,
        () => apply(right),
        () => apply(left)
      )
    }

  def empty[K,V]: Map[K,V] = EmptyMap()
}
