Map
===
Maps are ordered and implemented lazily. They will only be forced when doing
lookup. Currently they are implemented as simple Binary Search Trees. This
however, is due to change into an RB-Tree or similar at a later point. But the
nice thing about this is that the maps get the ordering for free.

Use just like a regular scala map. Key difference is that `apply` has changed
to

```scala
def apply(key: K): Option[V] = { ... }
```

so that there is no unsafe lookup.
