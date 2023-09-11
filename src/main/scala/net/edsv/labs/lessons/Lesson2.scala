package net.edsv.labs.lessons

import java.util.UUID
import java.util.concurrent.{Executors, TimeUnit}

object Lesson2 {

  def main(args: Array[String]): Unit = {

    val generatedUUIDfunc: () => UUID = {
      val random = UUID.randomUUID()
      () => random
    }

    println(generatedUUIDfunc())
    println(generatedUUIDfunc())

    def generatedUUIDmethod: () => UUID = {
      val random = UUID.randomUUID()
      () => random
    }

    println(generatedUUIDmethod())
    println(generatedUUIDmethod())
    //-----


    val range = 1 to 10
    range.foreach(println(_))
    range.foldLeft(0)(_ + _)
    //     val  les1:Lesson1;
    //    main()
    //    range.foldLeft(0)(sum)
    lazy val myLazyString: String = {
      println("I am ready")
      "Hello"
    }


    val executor = Executors.newSingleThreadScheduledExecutor()

    executor.schedule(new Runnable {
      override def run(): Unit = println(myLazyString)
    }, 1, TimeUnit.MINUTES)


    def calculateAndSend(int1: Int, int2: Int)(post: Int => Unit): Unit = post(int1 + int2)

    calculateAndSend(1, 2) { result => println(result) }

    //    CONDITIONS:
    val myString = ""
    val result = if (myString.nonEmpty) 1 else 0
    val result2: AnyVal = if (myString.nonEmpty) 1 else {}
    val unit: Unit = ()
    //    AnyVal v;
    val nullableInt: Int = null.asInstanceOf[Int]
    val optionalInt: Option[Int] = None
    val condition = true

  }

}
