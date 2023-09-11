package net.edsv.labs.lessons

object Lesson1 {

  def main(args: Array[String]): Unit = {
    var myChangableString = "name" + 1
    val myFinalString = "name" + 1

    myChangableString = "new value"


    def sum(v1: Int, v2: Int) = v1 + v2

    val sum3= (v1: Int, v2: Int) => v1 + v2
    val sum2= (v1: Int, v2: Int) => sum _

  println(Int.MaxValue,Int.MaxValue)
  }

}
