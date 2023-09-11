package net.edsv.labs.htask.hometask10.oop

import net.edsv.labs.htask.hometask10.oop.handler.RequestHandlerImpl
import net.edsv.labs.htask.hometask10.oop.mappers.Mappers
import net.edsv.labs.htask.hometask10.oop.request.SignUpRequest
import net.edsv.labs.htask.hometask10.oop.response.SignUpResponse
import net.edsv.labs.htask.hometask10.oop.services.{HashService, LoginService}

import scala.language.postfixOps

object HomeTask extends App with Mappers {


  /*
    Using all this infrustructure and fraims implement handler for SignUp operation
    1) validation, using rules declare in scala doc above SignUpRequest. All validation errors must be gathered together
    with help of ValidationError
    2) implement mapper for converting it to common SignUpDto and hash password
    3) implement processor(only simple stub which immediately returns OK answer)
    Write test for validator and mapper components
   */


  private val requestHandlerImpl = new RequestHandlerImpl(new LoginService(), new HashService())
  implicit val mapper = signUpRequestMapper

  private val errorOrResponse: Either[errors.Error, SignUpResponse] = requestHandlerImpl.handle(SignUpRequest(
    Option.empty,
    //            Option("John"),
    Option("Smith"),
    Option("johny%"),
    //    Option("johny"),
    Option("poh"),
    Option("911")
  ))

  println("result" + errorOrResponse.swap.map(e => e.errorMessage).swap)

}




