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

sealed trait ClassParamAnnotation extends StaticAnnotation
final class hold extends ClassParamAnnotation

final private[dilate] case class ExtractionResult(
  template: CompanionObjectTemplate,
  domain:   OwnerClassArgs)

final private[dilate] case class CompanionObjectTemplate(
  valueclasses:    Seq[Defn.Class] = Seq.empty,
  traits:          Seq[Defn.Trait] = Seq.empty,
  types:           Seq[Defn.Type]  = Seq.empty,
  implicitClasses: Seq[Defn.Class] = Seq.empty,
  implicitDefs:    Seq[Defn.Def]   = Seq.empty
)

final private[dilate] case class OwnerClassArgs(finalArgs: Seq[Seq[Term.Param]] = Seq.empty)

final private[dilate] case class OwnerClassArgsSplitted(
  nonImplicitArgs: Seq[Term.Param],
  implicitArgs:    Seq[Term.Param])

final private[dilate] case class ExtractionPreResult(
  extraction: Seq[Extraction],
  newArgs:    OwnerClassArgs)

final private[dilate] case class Extraction(
  newArgs:     Term.Param,
  valueclass:  Option[Defn.Class]    = None,
  traitT:      Option[Defn.Trait]    = None,
  typeT:       Option[Defn.Type]     = None,
  implicitDef: Seq[Option[Defn.Def]] = Seq.empty
)
