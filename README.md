## Dilate

[![Build Status](https://travis-ci.org/vitorsvieira/dilate.svg?branch=master)](https://travis-ci.org/vitorsvieira/dilate)
[![Software License](https://img.shields.io/badge/license-Apache 2-brightgreen.svg?style=flat)](LICENSE)

### Overview
Dilate provides macro annotations that generates value classes and unboxed tagged types at compile-time for extra type-safety focusing on nearly zero runtime overhead.
Value classes and tagged types have an important role when developing high performance Scala applications.

This project proudly uses [scalameta](https://github.com/scalameta/scalameta).

### Motivation

  - Type-safety and type checking
  - Avoid runtime object allocation
  - Zero syntactical and bytecode overhead

### Getting Started

To get started with SBT, simply add the following to your `build.sbt` file:

```scala
libraryDependencies += "com.vitorsvieira" %% "dilate" % "0.1.1"

resolvers += Resolver.bintrayIvyRepo("scalameta", "maven")

// A dependency on macro paradise is required to both write and expand
// new-style macros.  This is similar to how it works for old-style macro
// annotations and a dependency on macro paradise 2.x.
addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-beta4" cross CrossVersion.full)

scalacOptions += "-Xplugin-require:macroparadise"
```

### Examples

#### @valueclass

Applying `@valueclass` to the `BankAccount` class as below:

```scala
@valueclass case class BankAccount(
    number:      BigInt          = 10,
    funds:       BigDecimal,
    withdrawals: Seq[BigDecimal],
    token:       java.util.UUID) {

  def methodA = number * 1000
}

object BankAccount {
  def renew(account: BankAccount) = account.copy(token = java.util.UUID.randomUUID())
}
```

Allows `BankAccount` to be instantiated using the new types as: 

```scala
val account = BankAccount(
  BankAccount.Number(1234),
  BankAccount.Funds(10),
  BankAccount.Withdrawals(Seq(1000)),
  BankAccount.Token(java.util.UUID.randomUUID())
)
```

The above construction is possible as the following types(value classes) and implicit conversions are generated at compile-time.

```scala
case class BankAccount(
  number:      BankAccount.Number = BankAccount.Number(10),
  funds:       BankAccount.Funds,
  withdrawals: BankAccount.Withdrawals,
  token:       BankAccount.Token) { 
  def methodA = number * 1000 
}
object BankAccount {
  final case class Number(self: BigInt) extends AnyVal
  final case class Funds(self: BigDecimal) extends AnyVal
  final case class Withdrawals(self: _root_.scala.collection.Seq[BigDecimal]) extends AnyVal
  final case class Token(self: java.util.UUID) extends AnyVal
  
  private[this] implicit def toNumber(number: BigInt): BankAccount.Number = BankAccount.Number(number)
  implicit def toBigIntfromNumber(number: Number): BigInt = number.self
  
  private[this] implicit def toFunds(funds: BigDecimal): BankAccount.Funds = BankAccount.Funds(funds)
  implicit def toBigDecimalfromFunds(funds: Funds): BigDecimal = funds.self
  
  private[this] implicit def toWithdrawals(withdrawals: Seq[BigDecimal]): BankAccount.Withdrawals = BankAccount.Withdrawals(withdrawals)
  implicit def toSeqBigDecimalfromWithdrawals(withdrawals: Withdrawals): Seq[BigDecimal] = withdrawals.self
  
  private[this] implicit def toToken(token: java.util.UUID): BankAccount.Token = BankAccount.Token(token)
  implicit def tojavautilUUIDfromToken(token: Token): java.util.UUID = token.self
  
  def renew(account: BankAccount) = account.copy(token = java.util.UUID.randomUUID())
}
```

#### @valueclass with @hold

`@hold` allows keeping the type without any modification. Applying `@valueclass` to the `Person` class as below:

```scala
case class Age(value: Int) extends AnyVal

@valueclass final class Person(
  v1:           Boolean,
  @hold v2:     Age                 = Age(1),//will hold Age as is
  v3:           Int                 = 1,
  v4:           Int,
  bankAccount: BankAccount.Number)
```

Generates the following types as value classes, and implicit conversions at compile-time.

```scala
sealed class Person(
  v1:          Person.V1,
  v2:          Age          = Age(1),        //held the type Age from the above value class 
  v3:          Person.V3    = Person.V3(1),
  v4:          Person.V4,
  bankAccount: BankAccount.Number)
  
object Person {
  final case class V1(self: Boolean) extends AnyVal
  final case class V3(self: Int) extends AnyVal
  final case class V4(self: Int) extends AnyVal
  
  private[this] implicit def toV1(v1: Boolean): Person.V1 = Person.V1(v1)
  implicit def toBooleanfromV1(v1: V1): Boolean = v1.self
  
  private[this] implicit def toV3(v3: Int): Person.V3 = Person.V3(v3)
  implicit def toIntfromV3(v3: V3): Int = v3.self
  
  private[this] implicit def toV4(v4: Int): Person.V4 = Person.V4(v4)
  implicit def toIntfromV4(v4: V4): Int = v4.self
}
```

#### @newtype

Applying `@newtype` to the `BankAccount` class as below:

```scala
import BankAccount._
@newtype case class BankAccount(
  activated:     Boolean           = true,
  number:        BigInt,
  funds:         BigDecimal,
  withdrawals:   Seq[BigDecimal],
  token:         java.util.UUID,
  @hold manager: String)
```

Allows `BankAccount` to be created like:

```scala
val bankAccount2 = OtherBankAccount(
  number      = BigInt(10).number,
  funds       = BigDecimal(10).funds,
  withdrawals = Seq(BigDecimal(10)).withdrawals,
  token       = java.util.UUID.randomUUID().token,
  manager     = "test"
)
```
Due to current limitations in the [whiteboxing](http://docs.scala-lang.org/overviews/macros/blackbox-whitebox.html) architecture, only when using `@newtype` macro, the construction must have named arguments.


The above construction is possible as the following `Unboxed Tagged Types` and implicit classes are generated at compile-time.

```scala
import BankAccount._
case class BankAccount(
  activated:   BankAccount.Activated   = true.activated,
  number:      BankAccount.Number, 
  funds:       BankAccount.Funds,
  withdrawals: BankAccount.Withdrawals,
  token:       BankAccount.Token,
  manager:     String)
  
object BankAccount {
  trait ActivatedTag
  trait NumberTag
  trait FundsTag
  trait WithdrawalsTag
  trait TokenTag
  
  type Activated   = Boolean         @@ ActivatedTag
  type Number      = BigInt          @@ NumberTag
  type Funds       = BigDecimal      @@ FundsTag
  type Withdrawals = Seq[BigDecimal] @@ WithdrawalsTag
  type Token       = java.util.UUID  @@ TokenTag
  
  implicit class TaggedBoolean(val value: Boolean) extends AnyVal { 
    def activated: Activated = value.asInstanceOf[Activated] 
  }
  implicit class TaggedBigInt(val value: BigInt) extends AnyVal { 
    def number: Number = value.asInstanceOf[Number] 
  }
  implicit class TaggedBigDecimal(val value: BigDecimal) extends AnyVal { 
    def funds: Funds = value.asInstanceOf[Funds] 
  }
  implicit class TaggedSeqBigDecimal(val value: Seq[BigDecimal]) extends AnyVal { 
    def withdrawals: Withdrawals = value.asInstanceOf[Withdrawals] 
  }
  implicit class TaggedjavautilUUID(val value: java.util.UUID) extends AnyVal { 
    def token: Token = value.asInstanceOf[Token] 
  }
}
```


Note that in the example above `import BankAccount._` is presented. 
This is required only when using `@newtype` annotation and specially for classes with default values and/or companion objects with values looking for implicit conversion.
This is required due to current limitations on macro whiteboxing.

All the examples above are available in the `examples` folder.


### References and Answers ##

#### Value Class

- [Value Classes and Universal Traits](http://docs.scala-lang.org/overviews/core/value-classes.html)
- [SIP-15 - Value Classes](http://docs.scala-lang.org/sips/completed/value-classes.html)
- [Compiling Scala for Performance - Iulian Dragos - 2010 - Thesis](https://infoscience.epfl.ch/record/150270/files/EPFL_TH4820.pdf)
- [Implementing value classes in Dotty, a compiler for Scala - Guillaume Martres - 2015 - Thesis](http://guillaume.martres.me/master_thesis.pdf)
- [OpenJDK - State of the Values("Java 10 Value Types")](http://cr.openjdk.java.net/~jrose/values/values-0.html)
- [OpenJDK - Project Valhalla("Java 10 Value Types")](http://openjdk.java.net/projects/valhalla/)
- [Typelevel - Machinist vs. value classes](http://typelevel.org/blog/2015/08/06/machinist.html)
- [Type all the things! - Julien Tournay](http://jto.github.io/articles/type-all-the-things/)
- [Adding Semantic to Base Types Parameters in Scala](https://coderwall.com/p/l-plmq/adding-semantic-to-base-types-parameters-in-scala)
- [Beware the Scala Value Class](http://blog.johnbnelson.com/beware-the-scala-value-class.html)
- [SO - Scala value class, use cases](http://stackoverflow.com/questions/40704525/scala-value-class-use-cases)
- [SO - Idiomatic approach to Scala Value Classes](http://stackoverflow.com/questions/27380720/idiomatic-approach-to-scala-value-classes)
- [SO - In these cases, the Scala value class will be “boxed”, right?](http://stackoverflow.com/questions/15860179/in-these-cases-the-scala-value-class-will-be-boxed-right)

#### Unboxed Tagged Types

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
