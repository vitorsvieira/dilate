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

  /**
   * This example uses @valueclass to provide type-safety and some `zero runtime allocation`.
   * The @valueclass macro has no restrictions regarding default argument values!
   *
   * Please refer to the readme for more information.
   */
  @valueclass case class BankAccount1(
      number:      BigInt          = 10,
      funds:       BigDecimal,
      withdrawals: Seq[BigDecimal],
      token:       java.util.UUID) {

    def methodA = number * 1000
  }

  object BankAccount1 {
    def renew(account: BankAccount1) = account.copy(token = java.util.UUID.randomUUID())
  }

  /**
   * This example uses @newtype to provide type-safety and `zero runtime allocation`.
   * Notice the object import on top of the class.
   * This import is temporarily required to provide default argument values
   * using the implicit conversion generated inside the object.
   *
   * Please refer to the readme for more information.
   */
  import BankAccount2._
  @newtype case class BankAccount2(
    activated:     Boolean         = true,
    number:        BigInt,
    funds:         BigDecimal,
    withdrawals:   Seq[BigDecimal],
    token:         java.util.UUID,
    @hold manager: String)

  object BankAccount2 {
    val field = "value"

    def renew(account: BankAccount2) = account.copy(token = java.util.UUID.randomUUID().token)
  }

  val account2 = BankAccount2(
    false.activated,
    BigInt(10).number,
    BigDecimal(10).funds,
    Seq(BigDecimal(10)).withdrawals,
    java.util.UUID.randomUUID().token,
    "test"
  )

  val isActivated: BankAccount2.Activated = true.activated
  val number: BankAccount2.Number = BigInt(10).number
  val funds: BankAccount2.Funds = BigDecimal(10).funds
  val withdrawals: BankAccount2.Withdrawals = Seq(BigDecimal(10)).withdrawals
  val token: BankAccount2.Token = java.util.UUID.randomUUID().token

  println(account2)

  /**
   * This example shows a class using an external value class as an argument
   * and skipping it using @hold annotation.
   *
   * Note in this example that Person uses BankAccount1.Number and BankAccount2.Number.
   *
   * Both types are not absorbed by Person and stay as the way they are.
   * In current implementation types that are already dependent on other type are not absorbed.
   *
   * Please refer to the readme for more information.
   */
  case class Age(value: Int) extends AnyVal

  @valueclass sealed class Person(
    v1:           Boolean,
    @hold v2:     Age                 = Age(1),
    v3:           Int                 = 1,
    v4:           Int,
    bankAccount1: BankAccount1.Number,
    bankAccount2: BankAccount1.Number)

}
