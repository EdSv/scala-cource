package net.edsv.labs.lessons

import scala.annotation.tailrec

object Lesson7 extends App {

  type LibraryComputationType = Int => Int

  def sum(a: Int, b: Int) = a + b

  val sumFunc = sum (_:Int,_:Int)

  //  computational done
  val r1 = sumFunc(1, 2)
  //
  val partialSumApplying = sumFunc(1, _: Int)

  val r2 = partialSumApplying(2)

  def adder(d: Int, func: LibraryComputationType) = func(d * 2)


  def sumTriple(a: Int, b: Int, c: Int) = a + b + c

  val sumTripleFunc = sumTriple _

  val sumTripleCurrFunc: Int => Int => Int => Int = sumTripleFunc.curried
  sumTripleCurrFunc(0)(1)(2)


  def biFunc(a: Int, b: Int, computationFunc: (Int, Int) => Int) = {
    computationFunc(a, b)
  }

  val sum = biFunc(1, 2, _ + _)

  //  inner method or func

  def factorial(num: Int): Int = {
    @tailrec
    def factorialCompute(n: Int, acc: Int): Int = {
      if (n <= 1) {
        acc
      } else
        factorialCompute(n - 1, acc * n)
    }

    factorialCompute(num, 1)
  }
}
