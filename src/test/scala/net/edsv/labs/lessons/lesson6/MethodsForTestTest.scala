package net.edsv.labs.lessons.lesson6

import net.edsv.labs.lessons.lesson6.MethodsForTest.sum
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, OptionValues, WordSpec}

//import org.scalatest.{BeforeAndAfterEach, FunSuite}
@RunWith(classOf[JUnitRunner])
class MethodsForTestTest extends WordSpec with OptionValues
  with Matchers {


//  import net.edsv.labs.lesson1.lesson6._

  "MethodsForTestTest" when {

    "sum" should {
      "calculate" in {
        sum(-1, -2) shouldEqual -3
      }
    }
  }

}
