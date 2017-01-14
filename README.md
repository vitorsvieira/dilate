## Dilate

[![Build Status](https://travis-ci.org/vitorsvieira/dilate.svg?branch=master)](https://travis-ci.org/vitorsvieira/dilate)
[![Software License](https://img.shields.io/badge/license-Apache 2-brightgreen.svg?style=flat)](LICENSE)

### Overview
Dilate is a library which provides macro annotations for extra type safety.


### Motivation

- Value classes are a mechanism in Scala to avoid allocating runtime objects

### Getting Started

To get started with SBT, simply add the following to your `build.sbt` file:

```scala
libraryDependencies += "com.vitorsvieira" %% "dilate" % "0.1.0"
```


### Special Thanks

- [Ólafur Geirsson](https://twitter.com/olafurpg)


## References and Q&A ##

### Value Class ###

- [Value Classes and Universal Traits](http://docs.scala-lang.org/overviews/core/value-classes.html)
- [SIP-15 - Value Classes](http://docs.scala-lang.org/sips/completed/value-classes.html)
- [Compiling Scala for Performance - Iulian Dragos - 2010 - Thesis](https://infoscience.epfl.ch/record/150270/files/EPFL_TH4820.pdf)
- [Implementing value classes in Dotty, a compiler for Scala - Guillaume Martres - 2015 - Thesis](http://guillaume.martres.me/master_thesis.pdf)
- [Typelevel - Machinist vs. value classes](http://typelevel.org/blog/2015/08/06/machinist.html)
- [Type all the things! - Julien Tournay](http://jto.github.io/articles/type-all-the-things/)
- [Adding Semantic to Base Types Parameters in Scala](https://coderwall.com/p/l-plmq/adding-semantic-to-base-types-parameters-in-scala)
- [State of the Values("Java 10 Value Types")](http://cr.openjdk.java.net/~jrose/values/values-0.html)
- [Project Valhalla("Java 10 Value Types")](http://openjdk.java.net/projects/valhalla/)
- [Beware the Scala Value Class](http://blog.johnbnelson.com/beware-the-scala-value-class.html)
- [Scala value class, use cases](http://stackoverflow.com/questions/40704525/scala-value-class-use-cases)
- [Idiomatic approach to Scala Value Classes](http://stackoverflow.com/questions/27380720/idiomatic-approach-to-scala-value-classes)
- [In these cases, the Scala value class will be “boxed”, right?](http://stackoverflow.com/questions/15860179/in-these-cases-the-scala-value-class-will-be-boxed-right)

### Unboxed Tagged Types ###

- [Haskell - newtype](https://wiki.haskell.org/Newtype)
- [Practical uses for Unboxed Tagged Types](http://etorreborre.blogspot.nl/2011/11/practical-uses-for-unboxed-tagged-types.html)
- [Unboxed newtype in Scala?](https://gist.github.com/milessabin/89c9b47a91017973a35f)
- [Shapeless Type Operators](https://github.com/milessabin/shapeless/blob/master/core/src/main/scala/shapeless/typeoperators.scala)
- [Tagged type](http://eed3si9n.com/learning-scalaz/Tagged+type.html)
- [Unboxed new types within Scalaz7](http://timperrett.com/2012/06/15/unboxed-new-types-within-scalaz7/)
- [Unboxed Tagged Angst](http://underscore.io/blog/posts/2014/01/29/unboxed-tagged-angst.html)
- [Type Tags vs. Value Classes](https://groups.google.com/forum/#!topic/scalaz/Py_IIfp9d2Q)
- [Typelevel hackery tricks in Scala](http://www.folone.info/blog/Typelevel-Hackery/)
- [Newtype vs. newtype in Scala](https://earldouglas.com/articles/newtype.html)
- [Scala Tagged types - Introduction](http://www.vlachjosef.com/tagged-types-introduction/)
- [Scala anomalies noticed while writing a unit test for type tags](https://groups.google.com/forum/#!msg/shapeless-dev/TOGq0QZnIvQ/IzDqEJHKZ9AJ)

## License ##

This code is open source software licensed under the [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0.html).
