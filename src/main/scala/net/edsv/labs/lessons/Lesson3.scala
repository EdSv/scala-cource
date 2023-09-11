package net.edsv.labs.lessons

object Lesson3 extends App {
  val seqInt = Seq(1, 2, 3, 4)

  seqInt.foreach(println)

  for (x <- seqInt) {
    println(x)
  }

  val result = seqInt.map(x => x * x)
  val result2 = for {n <- seqInt} yield n * n

  println(result)
  println("=====")
  println(result2)


  seqInt.flatMap(n => seqInt.map(m => s"$n*$m=${n + m}"))


  val forComprehension = for {
    a <- seqInt
    b <- seqInt
  } yield {
    s"$a*$b=${a + b}"
  }


  forComprehension.foreach(println)


  val forComprehensionV3 = for {
    a <- seqInt
    b <- seqInt
    c <- seqInt
  } yield {
    s"Vp$a*$b*$c=${a + b + c}"
  }


  val param1 = Some(1)
  //  val param2 = Some(2)
  val param2: Option[Int] = None
  val param3 = Some(3)

  val optionComprehension = for {
    a <- param1
    b <- param2
    c <- param3
  } yield a * b * c
  forComprehension.foreach(println)

  val seqInt2 = Seq(1, 2, 3, 4)

  //  RULES FOR FLATTENING
  val multiplier = (x: Int) => x * x

  //  seqInt2 map multiplier == seqInt2.flatMap(x => Seq(multiplier(x)))
  val filterResult = seqInt2 filter (_ % 2 == 0) map multiplier
  val withFilterResult = seqInt2 withFilter (_ % 2 == 0) map multiplier

  //same as withFilter
  for {
    x <- seqInt2 if x % x == 2
  } yield multiplier(x)


}
