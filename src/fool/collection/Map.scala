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

trait Map[K,+V] {
  import Map._
  protected def root: Entry[K,V]
}

object Map {
  def apply[K,V](elems: (K, V)*)(implicit ord: Ordering[K]): Map[K,V] = {
    val ordered = elems.sortBy(_._1).toVector
    //TODO: finish implementing construction, stdlib is specialized for Map4 -
    //maybe do that too?
    ???
  }

  def empty[K,V]: Map[K,V] = new Map[K, Nothing] {
    protected val root = EmptyEntry()
  }

  sealed trait Entry[K,+V] { self =>
    def map[B](f: V => B): Entry[K, B] = self match {
      case empty: EmptyEntry[_] => empty
      case node: NodeEntry[K,V] => NodeEntry(
        node.k,
        () => f(node.value),
        () => node.left.map(f),
        () => node.right.map(f)
      )
    }
  }

  case class EmptyEntry[K]() extends Entry[K, Nothing] {
    override def map[B](f: Nothing => B): Entry[K, Nothing] = this
  }

  case class NodeEntry[K,+V] private (
    val k: () => K,
    val v: () => V,
    val l: () => Entry[K,V],
    val r: () => Entry[K,V]
  ) extends Entry[K,V] {
    lazy val key = k()
    lazy val value = v()
    lazy val left = l()
    lazy val right = r()
  }
}
