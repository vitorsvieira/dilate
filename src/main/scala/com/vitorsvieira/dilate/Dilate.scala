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

import scala.collection.immutable.Seq
import scala.meta._
import scala.util.Try

object Dilate {

  sealed trait Domain extends Product with Serializable
  final case object NewType extends Domain
  final case object ValueClass extends Domain

  /*
   * receives owner class name and its parameter lists
   *  - extract valueclasses based on the parameters,
   *  - insert the valueclasses into the companion object
   *  - update the owner class with the new parameters based on the valueclasses
   */
  private[dilate] def valueclassApply(
    ownerClassName: Type.Name,
    params:         Seq[Seq[Term.Param]]
  ): ExtractionResult = {

    implicit val domain: Domain = ValueClass

    val result = parseArgs(ownerClassName, params)

    // valueclasses to be inserted into the companion object
    val companionObjectTemplate = CompanionObjectTemplate(
      valueclasses = result.extraction.flatMap(_.valueclass),
      implicitDefs = result.extraction.flatMap(_.implicitDef).flatten
    )

    ExtractionResult(template = companionObjectTemplate, domain = result.newArgs)
  }

  /*
   * receives owner class name and its parameter lists
   *  - extract tagged types based on the parameters,
   *  - insert the traits, types, implicit classes into the companion object
   *  - update the owner class with the new parameters based on the tagged types
   */
  private[dilate] def newtypeApply(
    ownerClassName: Type.Name,
    params:         Seq[Seq[Term.Param]]
  ): ExtractionResult = {

    implicit val domain: Domain = NewType

    val result = parseArgs(ownerClassName, params)

    // traits, types, implicit classes to be inserted into the companion object
    val companionObjectTemplate = CompanionObjectTemplate(
      traits          = result.extraction.flatMap(_.traitT),
      types           = result.extraction.flatMap(_.typeT),
      implicitClasses = buildImplicitClass(result.extraction)
    )

    ExtractionResult(template = companionObjectTemplate, domain = result.newArgs)
  }

  /* Split args into non-implicit and implicit and extract respective meta related to the given domain */
  private[this] def parseArgs(ownerClassName: Type.Name, params: Seq[Seq[Term.Param]])(implicit domain: Domain) = {

    // return args separated between implicit and non-implicit, not modified.
    val splittedArgs: OwnerClassArgsSplitted = partitionArgs(params)

    // return new args to be used in the owner class and companion object
    val extraction: Seq[Extraction] = extractArgs(ownerClassName, splittedArgs.nonImplicitArgs)

    // new arguments and the implicit args
    val newArgs: OwnerClassArgs = OwnerClassArgs(
      Seq(extraction.map(_.newArgs), splittedArgs.implicitArgs).filter(_.nonEmpty)
    )

    ExtractionPreResult(extraction, newArgs)
  }

  /* Splits Ctor.Primary params(class arguments) between implicits and non-implicits */
  private[this] def partitionArgs(params: Seq[Seq[Term.Param]]): OwnerClassArgsSplitted = {
    val p = params.flatten.partition {
      case Term.Param(Seq(Mod.Implicit()), _, _, _) ⇒ false
      case _                                        ⇒ true
    }
    OwnerClassArgsSplitted(p._1, p._2)
  }

  /* extract respective meta related to the given domain */
  private[this] def extractArgs(
    ownerClassName: Type.Name,
    params:         Seq[Term.Param]
  )(implicit domain: Domain): Seq[Extraction] =
    params.map { implicit param: Term.Param ⇒
      //debugParam(param) //DEBUG
      param.decltpe match {
        // match A.B
        case Some(quasi @ Type.Select(Term.Name(_), Type.Name(_))) ⇒
          Extraction(
            newArgs = Term.Param(
              param.mods,
              Term.Name(param.name.value),
              Option.apply(quasi),
              param.default
            )
          )

        // Match a.F[_*], a.b.F[_*], a.b..z.F[_*]
        case Some(quasi @ Type.Apply(Type.Select(namespace, Type.Name(name: String)), Seq(_*))) ⇒
          val path = getPath(s"${namespace.syntax}.$name", quasi.args.map(_.toString()))
          extraction(ownerClassName, path)

        // Match F[_*]
        case Some(quasi @ Type.Apply(Type.Name(name: String), Seq(_*))) ⇒
          val path = getPath(name, quasi.args.map(_.toString()))
          extraction(ownerClassName, path)

        // Match tuple syntactic sugar (_), (_, _), (_, _, ...)
        case Some(quasi @ Type.Tuple(Seq(_*))) ⇒
          val path = getPath(s"Tuple${quasi.args.length}", quasi.args.map(_.toString()))
          extraction(ownerClassName, path)

        case _ ⇒
          extraction(ownerClassName, param.decltpe.fold("Any")(_.syntax))
      }
    }

  /* Hold args types with @hold annotation in the owner class*/
  private[this] def extraction(ownerClassName: Type.Name, path: String)(implicit domain: Domain, param: Term.Param) =
    (param, domain) match {
      case (Term.Param(mods, _, _, _), _) if mods.exists(m ⇒ m.syntax == "@hold") ⇒
        Extraction(newArgs = param.copy(mods = param.mods.filter(m ⇒ m.syntax != "@hold")))
      case (_, ValueClass) ⇒
        Extraction(
          newArgs     = buildOwnerClassArgs(ownerClassName),
          valueclass  = buildValueClass(Type.Name(path)),
          implicitDef = buildImplicitDef(ownerClassName)
        )
      case (_, NewType) ⇒
        Extraction(
          newArgs = buildOwnerClassArgs(ownerClassName),
          traitT  = buildTrait,
          typeT   = buildType
        )
    }

  /* build owner class args */
  private[this] def buildOwnerClassArgs(typeName: Type.Name)(implicit domain: Domain, param: Term.Param) = domain match {
    case ValueClass ⇒ Term.Param(
      mods    = param.mods,
      name    = Term.Name(param.name.value),
      decltpe = Some(Type.Name(s"$typeName.${param.name.value.capitalize}")),
      default = param.default.map(
        term ⇒ q"""${Ctor.Ref.Name(s"$typeName.${param.name.value.capitalize}")}($term)"""
      )
    )
    case NewType ⇒ Term.Param(
      mods    = param.mods,
      name    = Term.Name(param.name.value),
      decltpe = Some(Type.Name(s"$typeName.${param.name.value.capitalize}")),
      default = param.default.map(
        term ⇒ q"""$term.${Term.Name(param.name.value.head.toLower + param.name.value.tail)}"""
      )
    )
  }

  /* build value class */
  private[this] def buildValueClass(typeName: Type.Name)(implicit param: Term.Param): Option[Defn.Class] = {

    val valueClassTerm: Term.Param = Term.Param.apply(
      mods    = Seq.empty,
      name    = Term.Name("self"),
      decltpe = Option.apply(typeName),
      default = None
    )
    Try(q"""${Mod.Final.apply()} ${Mod.Case.apply()} class ${
      Type.Name(param.name.value.capitalize)
    }($valueClassTerm) extends AnyVal""").toOption
  }

  /* Build implicit conversion methods for valueclass */
  private[this] def buildImplicitDef(typeName: Type.Name)(implicit param: Term.Param): Seq[Option[Defn.Def]] = {

    val name = param.name.value
    val path = s"$typeName.${name.capitalize}"

    def buildDef(
      mods:       Seq[Mod],
      methodName: String,
      argType:    Option[Type.Arg],
      decltpe:    Option[Type.Name],
      body:       Term
    ): Option[Defn.Def] =
      Try(
        Defn.Def.apply(
          mods    = mods,
          name    = Term.Name(methodName),
          tparams = Nil,
          paramss = Seq(
            Seq(
              Term.Param.apply(
                mods    = Seq.empty,
                name    = Term.Name(name),
                decltpe = argType,
                default = None
              )
            )
          ),
          decltpe = decltpe,
          body    = body
        )
      ).toOption

    val toValueClass: Option[Defn.Def] = buildDef(
      mods       = Seq(mod"private[this]", Mod.Implicit.apply()),
      methodName = s"to${name.capitalize}",
      argType    = param.decltpe,
      decltpe    = Option.apply(Type.Name(path)),
      body       = q"""${Ctor.Ref.Name(path)}(${Term.Name(name)})"""
    )

    val fromValueClass: Option[Defn.Def] = {
      buildDef(
        mods       = Seq(Mod.Implicit.apply()),
        methodName = s"to${param.decltpe.get.syntax.replaceAll("[^a-zA-Z0-9]", "")}from${name.capitalize}",
        argType    = Option.apply(Type.Name.apply(s"${name.capitalize}")),
        decltpe    = Option.apply(Type.Name(param.decltpe.get.syntax)),
        body       = Term.Name(s"$name.self")
      )
    }

    Seq(toValueClass, fromValueClass)
  }

  /* Build trait tag for newtype */
  private[this] def buildTrait(implicit param: Term.Param): Option[Defn.Trait] =
    Try(q"trait ${Type.Name(s"${param.name.value.capitalize}Tag")}").toOption

  /* build tagged type for newtype */
  private[this] def buildType(implicit param: Term.Param): Option[Defn.Type] =
    Try(
      q"type ${Type.Name(param.name.value.capitalize)} = ${
        Type.Name(param.decltpe.get.syntax)
      } @@ ${Type.Name(s"${param.name.value.capitalize}Tag")}"
    ).toOption

  /* group implicit classes by type and generate method to restrict type domain*/
  private[this] def buildImplicitClass(extractions: Seq[Extraction]): Seq[Defn.Class] = {

    val types = extractions.flatMap(_.typeT).map {
      case q"type ${ name } = ${ tpe } @@ $_" ⇒
        val conversion =
          q"""
          def ${
            Term.Name(name.value.head.toLower + name.value.tail)
          }:$name = value.asInstanceOf[$name]
          """
        (tpe, conversion)
    }

    types.map(t ⇒ t._1.syntax).distinct.map { tpe ⇒
      q"""implicit class ${
        Type.Name(s"Tagged${tpe.replaceAll("[^a-zA-Z0-9]", "")}")
      }(val value: ${Type.Name(tpe)}) extends AnyVal{
            ..${types.filter(t ⇒ t._1.syntax == tpe).map(_._2)}
      }"""
    }
  }

  /* return opinionated F[_] path */
  private[this] def getPath(f: String, args: Seq[String]) = f match {
    case "Option"           ⇒ s"_root_.scala.Option[${args.head}]"
    case "Some"             ⇒ s"_root_.scala.Some[${args.head}]"
    case "List"             ⇒ s"_root_.scala.collection.immutable.List[${args.head}]"
    case "Seq"              ⇒ s"_root_.scala.collection.Seq[${args.head}]"
    case "Map"              ⇒ s"_root_.scala.collection.Map[${args.mkString(",")}]"
    case "Set"              ⇒ s"_root_.scala.collection.immutable.Set[${args.head}]"
    case "IndexedSeq"       ⇒ s"_root_.scala.collection.IndexedSeq[${args.head}]"
    case "Iterator"         ⇒ s"_root_.scala.collection.Iterator[${args.head}]"
    case "BufferedIterator" ⇒ s"_root_.scala.collection.BufferedIterator[${args.head}]"
    case "TraversableOnce"  ⇒ s"_root_.scala.collection.TraversableOnce[${args.head}]"
    case "Traversable"      ⇒ s"_root_.scala.collection.Traversable[${args.head}]"
    case "Iterable"         ⇒ s"_root_.scala.collection.Iterable[${args.head}]"
    case "Stream"           ⇒ s"_root_.scala.collection.immutable.Stream[${args.head}]"
    case "Vector"           ⇒ s"_root_.scala.collection.immutable.Vector[${args.head}]"
    case "Equiv"            ⇒ s"_root_.scala.math.Equiv[${args.head}]"
    case "Fractional"       ⇒ s"_root_.scala.math.Fractional[${args.head}]"
    case "Integral"         ⇒ s"_root_.scala.math.Integral[${args.head}]"
    case "Numeric"          ⇒ s"_root_.scala.math.Numeric[${args.head}]"
    case "Ordered"          ⇒ s"_root_.scala.math.Ordered[${args.head}]"
    case "Ordering"         ⇒ s"_root_.scala.math.Ordering[${args.head}]"
    case "PartialOrdering"  ⇒ s"_root_.scala.math.PartialOrdering[${args.head}]"
    case "PartiallyOrdered" ⇒ s"_root_.scala.math.PartiallyOrdered[${args.head}]"
    case "Either"           ⇒ s"_root_.scala.util.Either[${args.mkString(",")}]"
    case "Left"             ⇒ s"_root_.scala.util.Left[${args.mkString(",")}]"
    case "Right"            ⇒ s"_root_.scala.util.Right[${args.mkString(",")}]"
    case c                  ⇒ s"$c[${args.mkString(",")}]"
  }
}
