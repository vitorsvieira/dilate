/*
 * Copyright 2017 Vitor S. Vieira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vitorsvieira

package object dilate {

  // More information about Unboxed Tagged Types in the readme.
  // Unboxed newtypes, credit to @milessabin and @retronym
  type Tagged[U] = { type Tag = U }
  type @@[T, U] = T with Tagged[U]

  //  sealed trait ManipulateParamAnnotation extends StaticAnnotation
  //  sealed trait NumericValidationAnnotation extends StaticAnnotation
  //  sealed trait StringValidationAnnotation extends StaticAnnotation
  //  sealed trait OptionValidationAnnotation extends StaticAnnotation
  //  sealed trait CollectionValidationAnnotation extends StaticAnnotation
  //

  //  final class camelCase extends ValueClassParamAnnotation
  //  final class nonEmpty extends ValueClassParamAnnotation
  //  final class min extends ValueClassParamAnnotation
  //  final class max extends ValueClassParamAnnotation
  //
  //  final class nonZero extends NumericValidationAnnotation
  //  final class negative extends NumericValidationAnnotation
  //  final class positive extends NumericValidationAnnotation
  //
  //  final class isDefined extends OptionValidationAnnotation
  //
  //  final class obfuscate extends ManipulateParamAnnotation
  //  final class md5 extends ManipulateParamAnnotation
  //  final class base64 extends ManipulateParamAnnotation
  //  final class sha extends ManipulateParamAnnotation
  //  final class aes extends ManipulateParamAnnotation
  //  final class des extends ManipulateParamAnnotation
  //  final class blowfish extends ManipulateParamAnnotation
  //
  // val convertFromValueClass: Option[Defn.Def] =
  //    buildDef(
  //      methodName = s"to${param.decltpe.get.syntax}",
  //      argType = Option.apply(Type.Name.apply(s"${name.capitalize}")),
  //      decltpe = Option.apply(Type.Name(param.decltpe.get.syntax)),
  //      body = Term.Name(s"$name.self")
  //    )
  //class positive extends StaticAnnotation {
  //  inline def apply(defn: Any): Any = meta {
  //
  //    defn match {
  //      case cls@Defn.Class(_, _, _, Ctor.Primary(_, _, paramss), template) =>
  //        paramss.flatten.foreach(param => Coercion.debugParam(param))
  //        val toMap = q"""def mydef: String = ${Lit.apply("something")}"""
  //        val templateStats: Seq[Stat] = toMap +: template.stats.getOrElse(Nil)
  //        cls.copy(templ = template.copy(stats = Some(templateStats)))
  //      case _ =>
  //        println(defn.structure)
  //        abort("@positive must annotate a class argument.")
  //    }
  //  }
  //}

  // power-constructor
  // kleisi boilerplate
  // ExecutionContext
  // akka

  // 1 - power-constructor 'disambiguate'
  //  http://stackoverflow.com/questions/28635858/how-to-disambiguate-case-class-creation-with-multiple-parameter-lists
  //  case class Foo private (first: List[Int], second: List[Int])
  //
  //  object Foo {
  //    def apply(first: Int*) = new Foo(first.toList, List.empty[Int]) with NoSecond
  //
  //    trait NoSecond {
  //      self: Foo =>
  //      def apply(second: Int*) = new Foo(first.toList, second.toList)
  //    }
  //  }
  //
  //  val d1: NoSecond = Foo(1, 2, 3)
  //  val d2: Foo = Foo(1, 2, 3)(4, 5, 6)

  //.map {
  //  case param@Term.Param(mods@Seq(Mod.Implicit()), paramname, atpeopt, expropt) =>
  //    println(s"\n\nCOM-IMPLICIT $mods | $paramname | $atpeopt | $expropt\n")
  //  case param@Term.Param(mods, paramname, atpeopt, expropt) =>
  //    println(s"\n\nSEM-IMPLICIT $mods | $paramname | $atpeopt | $expropt\n")
  //}

  //q"..$modifiers class $name[..$tparams] ..${ctor.mods} (...${ctor.paramss}) extends $template"

  //  @typed protected class P3(age: Int)
  //  @typed class P1(i: Boolean)
  //  @typed case class P3(age: P1)
  //  val p = P3(P3.Age(P1(1)))
  //  @typed case class P2(age: Int)

  //  val mem1 = config(
  //    Key.exec.benchRuns -> 20
  //  ) withWarmer {
  //      warmer
  //    } withMeasurer (new Measurer.MemoryFootprint) measure {
  //      (1 to 1000000).foreach(i => P1(i))
  //    }
  //  println(s"Total memory: $mem1")
  //
  //  val gc1 = config(
  //    Key.exec.benchRuns -> 20
  //  ) withWarmer {
  //      warmer
  //    } withMeasurer (new Measurer.GarbageCollectionCycles, Aggregator.median[Int]) measure {
  //      (1 to 150000000).foreach(i => P1(i))
  //    }
  //  println(s"Total gcs: $gc1")

  //  trait A
  //  trait B
  //
  //  @typed case class P1var(var age: Seq[Int])
  //  @typed case class P1val(val age: Seq[Int])
  //  @typed class P2var(var age: Seq[Int])
  //  @typed class P2val(val age: Seq[Int])
  //  @typed case class Person3(age: Int = 42)
  //  @typed class T1(a: Tuple2[Int, Int])
  //  @typed case class T1C(a: Tuple2[Int, Int])
  //  @typed case class PersonOpt1(age: Option[Int])
  //  @typed case class PersonOpt2(age: Option[Int] = None)
  //  @typed case class PersonOpt3(age: Option[Int] = Some(10))

  //  @typed case class BankAccount(number: BigInt, age: Int, funds: BigDecimal, token: UUID)
  //  object BankAccount {
  //    implicit def toNumber(number: BigInt): BankAccount.Number = BankAccount.Number(number)
  //    implicit def toAge(age: Int): BankAccount.Age = BankAccount.Age(age)
  //    implicit def toFunds(funds: BigDecimal): BankAccount.Funds = BankAccount.Funds(funds)
  //    implicit def toToken(uuid: UUID): BankAccount.Token = BankAccount.Token(uuid)
  //
  //    def renew(account: BankAccount) = account.copy(token = java.util.UUID.randomUUID())
  //  }
  //
  //  val b = new BankAccount(BigInt(4), 30, BigDecimal(1000), UUID.randomUUID())
  //  println(b)

  //@typed case class PersonOpt2(age: mutable.ListBuffer[Int])
  //
  //  @typed case class Person2(
  //    age: Person1.Age,
  //    movies: Set[String],
  //    friends: List[String],
  //    phoneNumbers: Map[Int, String]
  //  )
  //
  //  val movies = Person2.Movies(Set("Something"))
  //  val friends = Person2.Friends(List("Something"))
  //  val phoneNumbers = Person2.PhoneNumbers(Map(1234 → "mobile"))
  //
  //  val p2: Person2 = Person2.apply(Person1.Age(10), movies, friends, phoneNumbers)
  //
  //  println(p2)

  //  case class Person1(friends: Friends)
  //  case class Friends(value: List[String]) extends AnyVal
  //
  //  val p = Person1(friends = Friends(List.empty))

  //  @typed case class Person2(
  //    age:       Person1.Age,
  //    firstName: String      = "scala.meta"
  //  )
  //
  //  @typed case class Person3(
  //    age: Int = 42,
  //    firstName: String = "John",
  //    lastName: String = "Doe"
  //  )
  //
  //  @typed class Person4(
  //      age:       Int    = 42,
  //      firstName: String = "John",
  //      lastName:  String = "Doe"
  //    )(implicit i: Boolean, s: String)
  //    object Person4 {
  //      def something(): Boolean = true
  //    }

  //  case class ClassX(
  //    i1: Int @positive,
  //    i2: Int,
  //    i3: Int
  //  )

  //  /* print Term.Param properties */
  //  private[this] def debugParam(param: Term.Param): Unit = {
  //    val d =
  //      s"""|---------------------------------------------------
  //          |param.mods             : ${param.mods}
  //          |param.name             : ${param.name}
  //          |param.decltpe          : ${param.decltpe}
  //          |param.decltpe.structure:
  //          | -> ${param.decltpe.get.structure.split(",").mkString(",\n\t")}
  //          |
  //          |param.decltpe.syntax   : ${param.decltpe.get.syntax}
  //          |param.decltpe.children : ${param.decltpe.get.children}
  //          |param.decltpe.parent   : ${param.decltpe.get.parent}
  //          |param.decltpe.pos      : ${param.decltpe.get.pos}
  //          |param.default          : ${param.default}
  //          |param.pos              : ${param.pos}
  //          |param.parent           : ${param.parent}
  //          |param.children         : ${param.children}
  //          |param.tokens           : ${param.tokens}
  //          |param.syntax           : ${param.syntax}
  //          |param.structure:
  //          | -> ${param.structure.split(",").mkString(",\n\t")}
  //          """.stripMargin
  //    println(s"$d")
  //  }

  //Due to macro limitations params with default values are not available
  //val importStat = q"import ${Term.Name(name.value)}._"
}
