package net.edsv.labs.lessons

object Lesson6 extends App {

  val intSeq: Seq[Int] = 1 to 10

  //  simple constant
  val maybeInt: AnyVal = 10

  maybeInt match {
    case 1 => println(1)
    case 10 => println(10)
    case true => println("bool")
    case _ => println
  }

  //  case class unapply

  case class Person(name: String, age: Int)

  case class Person2(age: Int, name: String)

  val person: Any = Person("Andriy", 20)

  person match {
    case Person(name, age) => println()
    case _ =>

      //      tuple matching
      val simpleTuple = ("Andriy", 20)

      val toPerson: ((String, Int)) => Person = (Person.apply _) tupled
      val toPerson2: ((String, Int)) => Person = (Person.apply _) tupled


      toPerson("", 10)
      toPerson2("", 10)
      toPerson2(simpleTuple)

      val p: Person = toPerson(simpleTuple)

  }
}
