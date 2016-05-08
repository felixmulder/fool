List
====

In the style of lists has been augmented somewhat. There is now a distinction
between lists that are empty, and `NonEmptyLists[+A]`.

This effectively removes unsafe methods like `head` on empty lists. The `Seq` trait
which `List[+A]` derives from, offers an `apply(index: Int)` method - that instead of
returning the element will return an `Option[A]`.

Lists are now also lazy by default, which means that you're free to run
multiple maps, filters etc - without having to worry about performance.

```scala
val list = 1 :: 2 :: 3 :: Nil // List(1, ?)

list.map(_ * 2) // List(2, ?)
```
