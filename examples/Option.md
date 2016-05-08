Option
======

Option is the most similar to the regular Scala standard library. It does not
change any behavior, except removing `.get`!

```scala
val none: Option[Int] = Option(null) // None
none.map(_ * 2) // None

val some = Option(5)
some.map(_ * 2) // Some(10)
```

The implementation also adds several object functions like:

```scala
val list:    List[Option[Int]] = Some(1) :: Some(2) :: Some(3) :: Nil
val optList: Option[List[Int]] = Option.sequence(list)

val same = Option.traverse(list)(identity)
optList == same // true
```

And `map2`:

```scala
Option.map2(Some(1), Some(2)){ (a, b) => a + b } // Some(3)
```
