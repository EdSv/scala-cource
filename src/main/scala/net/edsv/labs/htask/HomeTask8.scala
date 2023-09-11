package net.edsv.labs.htask


import net.edsv.labs.htask.HomeTask8.PaymentDomain

import scala.annotation.tailrec
import scala.language.postfixOps
import scala.util.Random

/**
 * @author Eduard.Svitana
 * */
object HomeTask8 extends App {

  // your products
  trait Product

  case object CustomerInsurance extends Product

  case object LifeInsurance extends Product

  case object CreditPayment extends Product

  case object Charity extends Product

  case object CreditCardPayment extends Product

  type ProductType = Product

  //token information
  type PaymentToken = String

  type TokenProvider = Product => PaymentToken

  // your payment statuses
  trait Status

  case object Processed extends Status

  case object ClientMistake extends Status

  case object ServerMistake extends Status

  // if status code is absent in mapping
  case object Default extends Status

  // your initial request
  case class PaymentRequest(msisdn: String, productType: ProductType, tempCode: Int, paymentSum: Int)

  // your payment response
  case class PaymentResponse(code: Int, server: Int)

  // consolidated payment result
  case class PaymentResult(request: PaymentRequest, response: PaymentResponse)

  // final payment status
  case class PaymentStatus(request: PaymentRequest, status: Status)

  // NOT CHANGEABLE
  trait PaymentService {

    def geographicalPriority: Int

    def canProcess[T <: ProductType](product: T): Boolean

    def withdrawPayment(payment: PaymentRequest, tokenProvider: TokenProvider): PaymentResponse =
      someCostCalculation(payment)(prepared => send(prepared, tokenProvider(payment.productType)))

    protected def someCostCalculation(payment: PaymentRequest)(p: PaymentRequest => PaymentResponse): PaymentResponse = {
      // very difficult function
      p(payment)
    }

    protected def send(payment: PaymentRequest, token: PaymentToken): PaymentResponse = payment.tempCode match {
      case c if c >= 300 => PaymentResponse(500, geographicalPriority)
      case c if c % 2 == 0 => PaymentResponse(200, geographicalPriority)
      case c if c % 2 != 0 => PaymentResponse(400, geographicalPriority)

    }

    protected val products: Map[ProductType, Int] = Map(CustomerInsurance -> 1, LifeInsurance -> 2, CreditPayment -> 3, Charity -> 4)
  }

  trait PostPaymentService {

    def serviceStatus: Status

    def codesToProcess: Set[Int]

    def processResult(paymentResult: PaymentResult): Status =
    // do some post business logic
      serviceStatus
  }

  ////////////// YOUR available services   // NOT CHANGEABLE

  // pool of priority partner cluster system in your geographical position that can process request or you have to try with next one
  // if any of services can't process your request you have to save them fo further execution

  val serverPool: Seq[PaymentService] = Random.shuffle(1 to 4 map (x => new PaymentService {
    override val geographicalPriority: Int = x

    override def canProcess[T <: ProductType](product: T): Boolean = product match {
      case CreditCardPayment => false
      case _ => products.get(product).exists(_ <= geographicalPriority)
    }
  }))

  // HERE you can take a token   // NOT CHANGEABLE
  object SecurityServer {

    // secret token generation
    def generatePaymentToken(msisdn: String, product: Product, tempCode: Int): PaymentToken = {
      println("request to security server")
      (product.hashCode toString) + msisdn.hashCode + tempCode.hashCode()
    }
  }

  // input payments to proceed
  val payments = List(
    PaymentRequest("0670000001", CustomerInsurance, 222, 1000),
    PaymentRequest("0670000002", LifeInsurance, 122, 900),
    PaymentRequest("0670000003", CreditPayment, 121, 2000),
    PaymentRequest("0670000004", CreditPayment, 405, 2000),
    PaymentRequest("0670000005", Charity, 120, 100),
    PaymentRequest("0670000006", CreditCardPayment, 300, 500))

  //

  val statusCodes: Map[Status, Set[Int]] = Map(
    Processed -> Set(200, 201, 202, 204),
    ClientMistake -> Set(400, 401, 402, 403),
    ServerMistake -> Set(500, 501, 502, 503)
  )


  // your function for chaining you can use it in your code
  type BusinessDomain[-IN, +OUT] = PartialFunction[IN, OUT]

  def chainDomains[IN, OUT](domains: List[BusinessDomain[IN, OUT]], default: BusinessDomain[IN, OUT]): BusinessDomain[IN, OUT] = {
    @tailrec
    def chain(domains: List[BusinessDomain[IN, OUT]], acc: BusinessDomain[IN, OUT]): BusinessDomain[IN, OUT] = domains match {
      case Nil => acc
      case head :: tail => chain(tail, acc orElse head)
    }

    domains.headOption map (firstDomain => chain(domains.tail :+ default, firstDomain)) getOrElse default
  }

  type PaymentDomain = PartialFunction[PaymentRequest, PaymentResult]

  type PostPaymentDomain = PartialFunction[PaymentResult, PaymentStatus]

  // calculate this one
  // val result: Seq[PaymentStatus] = ???

  /*
  User story:

  1) Application use several partner servers to submit new payments in several time zones
  2) According to instance geographical position application ordered partner cluster servers with priority from nearest to more distant.
  servers of partners payment system.
  3) Partner payment server can deny some financial products sometimes for specific reasons, so if nearest server is not ready to process you pass it
  to the next one with next geographical priority which can process.
  4) If any server can't process catch it and store(logging it will be enough) for next reprocessing
  5) Then after payment was processed analyze answer's code in right PostPaymentService and calculate PaymentStatus. Mapping you can
  find in val statusCodes: Map[Status, Set[Int]]

  GENERAL TASK:
  implement next flow:

  PaymentRequest -> PartialFunction[PaymentRequest, PaymentResult] -> PartialFunction[PaymentResult, PaymentStatus] -> PaymentStatus
  So final function have to get PaymentRequest and return PaymentStatus !!!!!!!
  Resolve all payments with help of it.

  Tips:

  1)You have to call method canProcess[T <: Product](product: T): Boolean in PaymentService to ensure operation execution. This method not costly due to it cashing nature
  2)This task you can solve with help of design pattern "Chain of Responsibility"
  3) You can chain domains with help of "chainDomains" function
  4) Implement PostPaymentService to construct PostPaymentDomain
  5) Create methods that create PaymentDomain and PostPaymentDomain respectively
  6) use currying or PartialApplied Function to create TokenGenerator

  Mandatory conditions!!!!:
   Use ONLY PartialFunction to build execution flow
   */

  /////////////////////////////Implementation///////////////////////////////////////


  def setupPaymentDomain(serverPool: Seq[PaymentService]) = {

    val defaultDomain: PaymentDomain = {
      case x: PaymentRequest if x.productType == CreditCardPayment => PaymentResult(x, PaymentResponse(-1, -1))
    }

    serverPool
      .sortBy(x => x.geographicalPriority)
      .map(server => {
        val pf: PartialFunction[PaymentRequest, PaymentResult] = {
          case x: PaymentRequest if (server.canProcess(x.productType)) =>
            PaymentResult(x, server.withdrawPayment(x, SecurityServer.generatePaymentToken(x.msisdn, _, x.tempCode)))
        }
        pf
      }
      )
//      .foldLeft(defaultDomain)((pf1, pf2) => pf1 orElse (pf2))
          .reduce((pf1, pf2) => pf1 orElse (pf2)) orElse (defaultDomain)
  }

  class PostPaymentServiceImpl(status: Status) extends PostPaymentService {

    override def serviceStatus: Status = status

    override def processResult(paymentResult: PaymentResult): Status = {
      paymentResult match {
        case paymentResult: PaymentResult if
          codesToProcess.contains(paymentResult.response.code) => serviceStatus
        case PaymentResult(_, _) => Default
      }
    }

    override def codesToProcess: Set[Int] = statusCodes.getOrElse(serviceStatus, Set[Int]())

  }


  def postPaymentDomain(status: Status): PostPaymentDomain = {
    val domain = new PostPaymentServiceImpl(status)

    new PostPaymentDomain {
      override def isDefinedAt(x: PaymentResult): Boolean =
        domain.codesToProcess.contains(x.response.code)

      override def apply(x: PaymentResult): PaymentStatus = {
        PaymentStatus(x.request, domain.processResult(x))
      }

    }

    /*
//       Why isnt recognized as Partial Function?
       (x: PaymentResult) =>
          x match {
            case x if domain.codesToProcess.contains(x.response.code) => PaymentStatus(x.request, domain.processResult(x))
          }*/

  }

  val domainForProcessed = postPaymentDomain(Processed)
  val domainForClientMistake = postPaymentDomain(ClientMistake)
  val domainForServerMistake = postPaymentDomain(ServerMistake)
  val domainForDefault = postPaymentDomain(Default)

  val postPaymentFullDomain = domainForProcessed orElse (domainForClientMistake) orElse (domainForServerMistake) orElse (domainForDefault)

  //  initial straight version
  @tailrec
  def paymentFullProcessingFlowV1(paymentReq: PaymentRequest, serverPool: Seq[PaymentService]): PaymentStatus = {

    serverPool.toList.sortBy(x => x.geographicalPriority)

    match {
      case head :: tail => {
        if (head.canProcess(paymentReq.productType)) {
          val tokenProvider: TokenProvider = SecurityServer.generatePaymentToken(paymentReq.msisdn, _, paymentReq.tempCode)
          val paymentResponse: PaymentResponse = head.withdrawPayment(paymentReq, tokenProvider)
          postPaymentFullDomain(PaymentResult(paymentReq, paymentResponse))
        } else paymentFullProcessingFlowV1(paymentReq, tail)
      }
      case Nil => PaymentStatus(paymentReq, ServerMistake)

    }
  }

  //final version
  def paymentFullProcessingFlow(paymentDomain: PaymentDomain)(postPaymentDomain: PostPaymentDomain)(paymentReq: PaymentRequest) = {
    paymentDomain andThen (postPaymentDomain) apply (paymentReq)
  }

  val r1: PaymentStatus = paymentFullProcessingFlowV1(payments(2), serverPool)

  println("RESULT V1: " + r1)
  println("RESULT V2:")

  val processing = paymentFullProcessingFlow(setupPaymentDomain(serverPool))(postPaymentFullDomain)(_)
//  payments.map(processing).foreach(println)
    List().map(processing).foreach(println)


  val l1 =List(1,2)
}