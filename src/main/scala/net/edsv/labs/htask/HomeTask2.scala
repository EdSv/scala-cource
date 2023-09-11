package net.edsv.labs.htask


import scala.io.Source.fromFile
import scala.util.Try

/** @author Eduard Sv
*
* */
object HomeTask2 extends App {

  // 1) try to read file from external system over network (use method getFile with two columns: 1) msisdn, subscriber type)
  // and don't forget to close resource after usage!!!!!!


  // 2) try to enrich get Data using main source getDataFromMainSource

  // 3) if fail to execute step 2) go to alternative source and try once more ( use getDataFromAlternativeSource)

  // 4) if success to do so, try to send to 3-d party system all list

  // 5) Implement enrichAndSend method with proper Left(Error) type or Rigiht[Int] Quantity of msisdns send to our third party system

  // Conditions:
  // use only Try Monad to resolve all problems with exception handling
  // You can use any additional custom functions / methods
  // Don't use method Try monad methods as get, getOrElse, isSuccess, isFailure !!!!!

  /// ===============help code ======================

  trait Error

  case class NetworkException(x: String) extends Exception

  case object NetworkError extends Error // if sftp server not available

  case object SourceTemporaryUnavailableError extends Error // if main source main source unavailable

  case object AllSourceTemporaryUnavailableError extends Error //if all source were unavailable

  case object ThirdPartySystemError extends Error //if 3-d party system error

  case class TemporaryUnavailableException(string: String) extends Exception

  case class ThirdPartySystemException(string: String) extends Exception


  case class SubscriberInfo(msisdn: String, subscriberType: Int, isActive: Boolean)

  val fileSource = "src/main/resources/lesson4/externalSourceFile.txt"

  // do not change this methods !!!!
  @throws[NetworkException]
  def getFile(isRisky: Boolean, source: String) = if (isRisky) throw NetworkException("SFTP server network exception") else fromFile(source)

  @throws[TemporaryUnavailableException]
  def getActiveData(isRisky: Boolean, msisdns: Seq[String]) = if (isRisky) throw TemporaryUnavailableException("Temporary Unavailable Exception") else {
    msisdns.map(m => (m, if (m.toInt % 2 == 0) 1 else 0))
  }

  @throws[TemporaryUnavailableException]
  def getDataFromMainSource(isRisky: Boolean, msisdns: Seq[String]) = getActiveData(isRisky, msisdns)

  @throws[TemporaryUnavailableException]
  def getDataFromAlternativeSource(isRisky: Boolean, msisdns: Seq[String]) = getActiveData(isRisky, msisdns)

  def sendToProvider(isRisky: Boolean, msisdns: Seq[SubscriberInfo]) =
    if (isRisky) throw ThirdPartySystemException("third party system exception") else msisdns.foreach(m => s"Sent $m")

  // implement this one
  def enrichAndSend(getFileIsRisky: Boolean,
                    getDataFromMainSourceIsRisky: Boolean,
                    getDataFromAlternativeSourceIsRisky: Boolean,
                    sendToProviderIsRisky: Boolean,
                    fileSource: String): Either[Error, Int] = ???


  def enrichAndSendImpl(getFileIsRisky: Boolean,
                        getDataFromMainSourceIsRisky: Boolean,
                        getDataFromAlternativeSourceIsRisky: Boolean,
                        sendToProviderIsRisky: Boolean,
                        fileSource: String): Either[Error, Int] = {

      readFile(getFileIsRisky, fileSource)
        .flatMap(lines => Try(lines map parseFields))
        .flatMap(s =>
          Try(getDataFrom(getDataFromMainSourceIsRisky, s, getDataFromMainSource))
            .recoverWith {
              case exc =>
                println(exc)
                println("try alternative source")
                Try(getDataFrom(getDataFromAlternativeSourceIsRisky, s, getDataFromAlternativeSource))
            })
        .map(enriched => enriched.map(data => SubscriberInfo tupled data))
        .map(logReq)
        .flatMap(typed => Try(sendToProvider(sendToProviderIsRisky, typed)).map(_ => typed.size))
        .toEither
        .left.map(exc => map2error(exc))
  }


  def readFile(isRisky: Boolean, source: String) = {
    val resource = Try(getFile(isRisky, source))
    try {
      resource.map(res => res.getLines().toList)
    }
    finally {
      resource.map(b => b.close())
    }
  }

  case class WrongFormatException(string: String) extends Exception

  def parseFields(rawData: String) = {
    val arr = rawData.split(";")

    if (arr.size == 2) {
      val msisdn: String = arr.apply(0)
      val subsType: Int = Integer.parseInt(arr.apply(1))
      (msisdn, subsType)
    } else
      throw WrongFormatException(rawData)
  }

  def getDataFrom(isRisky: Boolean, msisdn: Seq[(String, Int)], source: (Boolean, Seq[String]) => Seq[(String, Int)]) = {
    for {
      r <- source(isRisky, msisdn.map(g => g._1))
      x <- msisdn if x._1 == r._1
    } yield (x._1, x._2, r._2 != 0)
  }

  def logReq[T](v: Seq[T]) = {
    println("data request :")
    v.foreach(println)
    v
  }

/*  def map2error(exc: Throwable): Error = {
    println(exc)
    if (exc.isInstanceOf[NetworkException])
      NetworkError
    else if (exc.isInstanceOf[TemporaryUnavailableException])
      AllSourceTemporaryUnavailableError
    else
      ThirdPartySystemError
  }*/

    def map2error(exc: Throwable): Error = {
      exc match {
        case _: NetworkException => NetworkError
        case _: TemporaryUnavailableException => SourceTemporaryUnavailableError
        case _: ThirdPartySystemException => ThirdPartySystemError
      }
    }

  val result = enrichAndSendImpl(false, getDataFromMainSourceIsRisky = true, getDataFromAlternativeSourceIsRisky = false, sendToProviderIsRisky = false, fileSource)

  println("result: ")
  println(result)

}
