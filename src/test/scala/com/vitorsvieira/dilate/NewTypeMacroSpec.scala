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

import OtherBankAccount._
@newtype case class OtherBankAccount(
    activated:     Boolean         = true,
    number:        BigInt,
    funds:         BigDecimal,
    withdrawals:   Seq[BigDecimal],
    token:         java.util.UUID,
    @hold manager: String) {

  val classField = "value"
  def classMethod: BigDecimal = funds * 1000
}

object OtherBankAccount {
  val field = "value"
  def renew(account: OtherBankAccount) = account.copy(token = java.util.UUID.randomUUID().token)
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
    val acc: OtherBankAccount = OtherBankAccount(
      //activated   = false.activated,
      number      = BigInt(123).number,
      funds       = BigDecimal(123).funds,
      withdrawals = Seq(BigDecimal(123)).withdrawals,
      token       = uuid.token,
      manager     = "test"
    )

    "use the tagged types inside the companion object" in {

      assert(acc.isInstanceOf[OtherBankAccount])
      assert(acc.activated.isInstanceOf[OtherBankAccount.Activated])
      assert(acc.number.isInstanceOf[OtherBankAccount.Number])
      assert(acc.funds.isInstanceOf[OtherBankAccount.Funds])
      assert(acc.withdrawals.isInstanceOf[OtherBankAccount.Withdrawals])
      assert(acc.token.isInstanceOf[OtherBankAccount.Token])
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

      assert(OtherBankAccount.renew(acc) !== uuid)
    }

    "have field members in the companion object still accessible after expansion/rewriting" in {

      assert(OtherBankAccount.field === "value")
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

      assert(activated === true)
      assert(acc.activated === true.activated)
      assert(acc.activated.isInstanceOf[OtherBankAccount.Activated])
    }
  }
}
