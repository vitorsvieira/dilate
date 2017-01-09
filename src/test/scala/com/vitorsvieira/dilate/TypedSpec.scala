package com.vitorsvieira.dilate

import scala.meta._
import org.scalatest._


@typed class ClassWithoutCompanion(age: Int)

@typed class ClassWithCompanion(age: Int)
object ClassWithCompanionObject{
  def myMethod(x:Int) = this
}

class TypedSpec extends FlatSpec with Matchers{

//  "@valueclass annotation" should{
//
//    "create companion object and create valueclass inside" should{
//
//
//    }
//
//    "create valueclass inside the existing companion object" should{
//      //      @typed case class Person(age: Int)
//
//    }
//  }
}
