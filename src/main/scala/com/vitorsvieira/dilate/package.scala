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

import scala.annotation.StaticAnnotation
import scala.collection.immutable.Seq
import scala.util.Try
import scala.meta._

package object dilate {

  //  sealed trait ValueClassParamAnnotation extends StaticAnnotation
  //  sealed trait ManipulateParamAnnotation extends StaticAnnotation
  //  sealed trait NumericValidationAnnotation extends StaticAnnotation
  //  sealed trait StringValidationAnnotation extends StaticAnnotation
  //  sealed trait OptionValidationAnnotation extends StaticAnnotation
  //  sealed trait CollectionValidationAnnotation extends StaticAnnotation
  //
  //  final class skip extends ValueClassParamAnnotation
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
}
