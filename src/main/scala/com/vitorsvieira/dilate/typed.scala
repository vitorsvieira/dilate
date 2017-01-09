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

import scala.annotation.StaticAnnotation
import scala.collection.immutable.Seq
import scala.meta._
import scala.util.Try


class typed extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      // match class with a companion object, class first then the companion
      case Term.Block(Seq(cls@Defn.Class(_, name, _, ctor, _), companion: Defn.Object)) ⇒
        val result       = Dilate.extract(name, ctor.paramss)
        val newClass: Defn.Class = cls.copy(
          ctor = Ctor.Primary.apply(ctor.mods, ctor.name, result.domain.finalArgs)
        )

        val templateStats: Option[Seq[Stat]] = Try(
          result.template.valueclasses ++:
            result.template.implicitDefs ++:
            companion.templ.stats.getOrElse(Nil)
        ).toOption

        val newCompanion: Defn.Object =
          companion.copy(templ = companion.templ.copy(stats = templateStats))

        println(s"\n$newClass\n$newCompanion\n")
        Term.Block(Seq(newClass,newCompanion))

      // match class with a companion object, companion first then class
//      case Term.Block(Seq(companion: Defn.Object, cls@Defn.Class(_, name, _, ctor, _))) ⇒
//        val result       = Dilate.extract(name, ctor.paramss)
//        val newClass: Defn.Class = cls.copy(
//          ctor = Ctor.Primary.apply(ctor.mods, ctor.name, result.domain.finalArgs)
//        )
//
//        val templateStats: Option[Seq[Stat]] = Try(
//          result.template.valueclasses ++:
//            result.template.implicitDefs ++:
//            companion.templ.stats.getOrElse(Nil)
//        ).toOption
//
//        val newCompanion: Defn.Object =
//          companion.copy(templ = companion.templ.copy(stats = templateStats))
//
//        println(s"\n$newClass\n$newCompanion\n")
//        Term.Block(Seq(newCompanion, newClass))
      // match class without a companion object
      case cls@Defn.Class(_, name, _, ctor, _)                                          ⇒
        val result: DilateResult = Dilate.extract(name, ctor.paramss)
        val newClass: Defn.Class = cls.copy(
          ctor = Ctor.Primary.apply(ctor.mods, ctor.name, result.domain.finalArgs)
        )

        val newCompanion: Defn.Object =
          q"""object ${Term.Name(name.value)} {
            ..${result.template.valueclasses}
            ..${result.template.implicitDefs}
          }"""

        println(s"\n$newClass\n$newCompanion\n")
        Term.Block(Seq(newClass, newCompanion))
      case _ ⇒
        println(defn.structure)
        abort("@valueclass must annotate a class.")
    }
  }
}
