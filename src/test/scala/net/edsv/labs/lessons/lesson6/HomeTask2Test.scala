package net.edsv.labs.lessons.lesson6


import net.edsv.labs.htask.HomeTask2.{NetworkError, NetworkException, SourceTemporaryUnavailableError, TemporaryUnavailableException, ThirdPartySystemError, ThirdPartySystemException, WrongFormatException, getActiveData, getDataFrom, getDataFromMainSource, getFile, map2error, parseFields}
import org.junit.Ignore
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, OptionValues, WordSpec}

import java.io.FileNotFoundException
import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class HomeTask2Test extends WordSpec with OptionValues
  with Matchers {


  "HomeTask2" when {

    "getFile" should {
      "throw NetworkException if risky" in {
        an[NetworkException] should be thrownBy
          getFile(true, "filepath")
      }


      "throw FileNotFoundException if filepath is wrong" in {
        an[FileNotFoundException] should be thrownBy
          getFile(false, "wrongfilepath")
      }


    }

    "getActiveData" should {
      "throw TemporaryUnavailableException if risky" in {
        an[TemporaryUnavailableException] should be thrownBy
          getActiveData(true, Seq("0673052785", "0673052787"))
      }

      "should map to 1 or 0" in {
        getActiveData(false, Seq("0673052785", "0673052786", "0673052787")) should equal(Seq(("0673052785", 0), ("0673052786", 1), ("0673052787", 0)))
      }
    }

    "parseFields" should {
      "parse tuple if correct format" in {
        parseFields("0673052785;1") should equal(("0673052785", 1))
      }
      "throw WrongFormatException if wrong format" in {
        an[WrongFormatException] should be thrownBy
          parseFields("0673052785,1")
      }
    }

    "map2error" should {
      "map exc to correct case obj" in {
        //        map2error(NetworkException("err")) shouldBe a[Any]
        map2error(NetworkException("err")) shouldBe (NetworkError)
        map2error(TemporaryUnavailableException("err")) shouldBe (SourceTemporaryUnavailableError)
        map2error(ThirdPartySystemException("err")) shouldBe (ThirdPartySystemError)
      }
    }

    "getDataFrom" should {

      "should process" in {
        val res = getDataFrom(false, Seq(("0673052788", 1), ("0673052789", 0)), getDataFromMainSource)
        res should equal(Seq(("0673052788", 1, true), ("0673052789", 0, false)))
      }
      "should throw specific exc" in {

      }
    }
  }

}
