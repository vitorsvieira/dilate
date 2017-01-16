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

import org.scalatest._
import Matchers._

import BankAccount2._
@newtype case class BankAccount2(
    activated:     Boolean         = true.activated,
    number:        BigInt,
    funds:         BigDecimal,
    withdrawals:   Seq[BigDecimal],
    token:         java.util.UUID,
    @hold manager: String) {

  val classField = "value"
  def classMethod: BigDecimal = funds * 1000
}

object BankAccount2 {
  val field = "value"

  def renew(account: BankAccount2) = account.copy(token = java.util.UUID.randomUUID().token)
}

class NewTypeMacroSpec extends WordSpec {

  "@newtype annotation" should {

    "compile only in a class" in {
      """
        |@newtype class Test
      """.stripMargin should compile
    }

    "not compile in a trait" in {
      """
        |@newtype trait Test
      """.stripMargin shouldNot compile
    }

    "not compile in an object" in {
      """
        |@newtype object Test
      """.stripMargin shouldNot compile
    }

    "not compile in an def" in {
      """
        |@newtype def Test = 10
      """.stripMargin shouldNot compile
    }

    "not compile in a val" in {
      """
        |@newtype val Test = 1
      """.stripMargin shouldNot compile
    }

    "not compile in a var" in {
      """
        |@newtype var Test = 1
      """.stripMargin shouldNot compile
    }

    "not compile in a type" in {
      """
        |@newtype type Test = Int
      """.stripMargin shouldNot compile
    }
  }

  "a class using @newtype" should {

    val uuid = java.util.UUID.fromString("673153b5-35b3-43bd-aa54-cea276130a48")

    val acc = BankAccount2(
      false.activated,
      BigInt(123).number,
      BigDecimal(123).funds,
      Seq(BigDecimal(123)).withdrawals,
      uuid.token,
      "test"
    )

    "use the tagged types inside the companion object" in {

      assert(acc.isInstanceOf[BankAccount2])
      assert(acc.activated.isInstanceOf[BankAccount2.Activated])
      assert(acc.number.isInstanceOf[BankAccount2.Number])
      assert(acc.funds.isInstanceOf[BankAccount2.Funds])
      assert(acc.withdrawals.isInstanceOf[BankAccount2.Withdrawals])
      assert(acc.token.isInstanceOf[BankAccount2.Token])
      assert(acc.manager.isInstanceOf[String])
    }

    "have implicit conversion for each type/valueclass inside the companion object" in {

      val number: BigInt = acc.number
      val funds: BigDecimal = acc.funds
      val withdrawals: Seq[BigDecimal] = acc.withdrawals
      val token: java.util.UUID = acc.token

      assert(number === 123)
      assert(funds === 123)
      assert(withdrawals === Seq(123))
      assert(token === uuid)
    }

    "have methods in the companion object still accessible after expansion/rewriting" in {

      assert(BankAccount2.renew(acc) !== uuid)
    }

    "have field members in the companion object still accessible after expansion/rewriting" in {

      assert(BankAccount.field === "value")
    }

    "have methods in the class still accessible after expansion/rewriting" in {

      assert(acc.classMethod === 123000)
    }

    "have field members in the class still accessible after expansion/rewriting" in {

      assert(acc.classField === "value")
    }

    "skip rewriting for arguments with @hold annotation" in {

      assert(acc.manager.isInstanceOf[String])
    }

    "implement default parameter value using the value class" in {

      val activated: Boolean = acc.activated

      assert(activated === false)
      assert(acc.activated === false.activated)
      assert(acc.activated.isInstanceOf[BankAccount2.Activated])
    }
  }
}
