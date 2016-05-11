fool
====
Functional Object-Oriented Library: A Scala Standard Library Replacement

[![Build Status](https://travis-ci.org/felixmulder/fool.svg?branch=master)](https://travis-ci.org/felixmulder/fool)
[![Coverage Status](https://coveralls.io/repos/github/felixmulder/fool/badge.svg?branch=master)](https://coveralls.io/github/felixmulder/fool?branch=master)

fool is a (not-ready-for-production) project which aims to cherry pick the good
parts of the standard library and kill of the bad parts.

The library aims for better type-safety and the removal of "exception"
programming. As such, there will be no unsafe operations like `None.get` or
`Nil.head`.

Most structures will also be transparently lazy and prefer immutability
over mutable structures.

The default list is lazy and printing it will only result in the first element
being evaluated, e.g: `List(1, ?)`.

Examples
--------
* [Option](examples/Option.md)
* [List](examples/List.md)


Ongoing
-------
* Implement `View` and `Iterable` (see branch:
  [topic/views](https://github.com/felixmulder/fool/tree/topic/views))
