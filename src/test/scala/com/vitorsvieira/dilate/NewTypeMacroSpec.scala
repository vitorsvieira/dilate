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
  @hold manager: String)

object BankAccount2 {
  val field = "value"

  def renew(account: BankAccount2) = account.copy(token = java.util.UUID.randomUUID().token)
}

class NewTypeMacroSpec extends WordSpec {

  //  "a class with @valueclass annotation" should {
  ////
  ////    val uuid = java.util.UUID.fromString("673153b5-35b3-43bd-aa54-cea276130a48")
  ////
  ////    val acc = BankAccount(
  ////      number      = BankAccount.Number(123),
  ////      funds       = BankAccount.Funds(123),
  ////      withdrawals = BankAccount.Withdrawals(Seq(123)),
  ////      token       = BankAccount.Token(uuid),
  ////      manager     = "Scala"
  ////    )
  //
  ////    "use the valueclass(es) inside the companion object" in {
  ////
  ////      assert(acc.activated.isInstanceOf[BankAccount.Activated])
  ////      assert(acc.isInstanceOf[BankAccount])
  ////      assert(acc.number.isInstanceOf[BankAccount.Number])
  ////      assert(acc.funds.isInstanceOf[BankAccount.Funds])
  ////      assert(acc.withdrawals.isInstanceOf[BankAccount.Withdrawals])
  ////      assert(acc.token.isInstanceOf[BankAccount.Token])
  ////      assert(acc.manager.isInstanceOf[String])
  ////    }
  //
  //    //      "have implicit conversion for each type/valueclass inside the companion object" in {
  //    //
  //    //        val number: BigInt = acc.number
  //    //        val funds: BigDecimal = acc.funds
  //    //        val withdrawals: Seq[BigDecimal] = acc.withdrawals
  //    //        val token: java.util.UUID = acc.token
  //    //
  //    //        assert(number === 123)
  //    //        assert(funds === 123)
  //    //        assert(withdrawals === Seq(123))
  //    //        assert(token === uuid)
  //    //      }
  //    //
  //    //      "have methods in the companion object still accessible after expansion/rewriting" in {
  //    //
  //    //        assert(BankAccount.renew(acc) !== uuid)
  //    //      }
  //    //
  //    //      "have field members in the companion object still accessible after expansion/rewriting" in {
  //    //
  //    //        assert(BankAccount.field === "value")
  //    //      }
  //    //
  //    //      "have methods in the class still accessible after expansion/rewriting" in {
  //    //
  //    //        assert(acc.classMethod === 123000)
  //    //      }
  //    //
  //    //      "have field members in the class still accessible after expansion/rewriting" in {
  //    //
  //    //        assert(acc.classField === "value")
  //    //      }
  //    //
  //    //      "skip rewriting for arguments with @hold annotation" in {
  //    //
  //    //        assert(acc.manager.isInstanceOf[String])
  //    //      }
  //    //
  //    //      "implement default parameter value using the value class" in {
  //    //
  //    //        val activated: Boolean = acc.activated
  //    //
  //    //        assert(activated === true)
  //    //        assert(acc.activated === BankAccount.Activated(true))
  //    //        assert(acc.activated.isInstanceOf[BankAccount.Activated])
  //    //      }
  //  }
}
