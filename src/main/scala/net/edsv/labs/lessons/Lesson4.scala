package net.edsv.labs.lessons

import java.util

object Lesson4 extends App {
  case class Person(name: String, age: Int)

  val personA = Person("Sasha", 20)
  //  same as
  val personB = Person.apply("Sasha", 21)
  val personC = personB.copy(age = 21)
  val personM = Person("Masha", 21)

  println(personA == personB)
  println(personA equals personB)
  println(personA eq personB)
  println(personA eq personA)

  val result = personC match {
    case Person(name, 20) => s"name=$name age=20"
    case Person(name, age) => s"name=$name"
    case _ => "no person"
  }
  println(result)

  case class Person2(names: util.ArrayList[String], age: Int)

}
