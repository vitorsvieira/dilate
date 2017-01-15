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

package com.vitorsvieira.dilate

object Examples extends App {

  //@valueclass case class P1var(var age: Seq[Int])

  case class Age(value: Int) extends AnyVal
  @valueclass sealed class P2(
    v1:       Boolean,
    @hold v2: Age     = Age(1),
    v3:       Int     = 1,
    v4:       Int
  )

  object Scoring {
    trait Design
    trait Usability
    trait Durability

    type DesignScore = Int @@ Design
    type UsabilityScore = Int @@ Usability
    type DurabilityScore = Int @@ Durability

    implicit class TaggedInt(val i: Int) extends AnyVal {
      def design: DesignScore = i.asInstanceOf[DesignScore]
      def usability: UsabilityScore = i.asInstanceOf[UsabilityScore]
      def durability: DurabilityScore = i.asInstanceOf[DurabilityScore]
    }
  }
  case class Scoring(
      design:     Scoring.DesignScore,
      usability:  Scoring.UsabilityScore,
      durability: Scoring.DurabilityScore) {

    def sum = design + usability + durability
  }
  import Scoring._

  val sc1 = Scoring(1.design, 1.usability, 1.durability)
  println(sc1.sum)

  //@newtype class Scoring2(ratingScore: Int, creditScore: Int)

  //  object BankAccount {
  //    def renew(account: BankAccount) = account.copy(token = java.util.UUID.randomUUID())
  //  }
  //  @typed case class BankAccount(
  //    number:      BigInt          = 10,
  //    funds:       BigDecimal,
  //    withdrawals: Seq[BigDecimal],
  //    token:       java.util.UUID
  //  )

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
}
