package net.edsv.labs.lessons

import scala.annotation.tailrec
import scala.collection.immutable.List

object Lesson8 extends App {

  val division = new PartialFunction[Int, Int] {
    override def isDefinedAt(x: Int): Boolean = ???

    override def apply(v1: Int): Int = ???
  }
  println(division(0))


  val division2: PartialFunction[Int, Int] = {
    case x if x != 0 => 100 / x
  }

  println(division2(0))


  val divisionNonZero: PartialFunction[Int, Either[Int, Int]] = {
    case x if x != 0 => Right[Int, Int](100 / x)
  }

  val alernative: PartialFunction[Int, Either[Int, Int]] = {
    case _ => Left(0)
  }

  val andThenFunc: PartialFunction[Either[Int, Int], Option[Int]] = {
    case x => x.toOption
  }

  val div = divisionNonZero orElse (alernative)

  println(div(0))

  type MyDomainType = PartialFunction[Int, Either[Int, Int]]

  def chainPartialFunc(domains: List[MyDomainType], acc: MyDomainType) {
    @tailrec
    def chainDomains(domains: List[MyDomainType], acc: MyDomainType): MyDomainType = domains match {
      case Nil => acc
      case head :: tail => chainDomains(tail, acc orElse head)
    }


  }
}