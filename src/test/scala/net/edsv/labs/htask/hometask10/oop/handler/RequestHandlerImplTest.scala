package net.edsv.labs.htask.hometask10.oop.handler

import net.edsv.labs.htask.hometask10.oop.dto.SignUpDto
import net.edsv.labs.htask.hometask10.oop.errors.{EmptyStringError, ValidationError, WrongSymbolStringError}
import net.edsv.labs.htask.hometask10.oop.processor.OK
import net.edsv.labs.htask.hometask10.oop.request.SignUpRequest
import net.edsv.labs.htask.hometask10.oop.response.SignUpResponse
import net.edsv.labs.htask.hometask10.oop.services.{HashService, LoginService}
import org.junit.runner.RunWith
import org.scalatest.{Matchers, OptionValues, WordSpec}
import org.scalatest.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class RequestHandlerImplTest extends WordSpec with OptionValues
  with Matchers {

  val requestHandlerImpl = new RequestHandlerImpl(new LoginService(), new HashService())
  implicit val mapper = (req: SignUpRequest) =>
    SignUpDto(req.name.get, req.surname.get, req.login.get, req.pass.get, req.msisdn.get)

  "HomeTask10" when {


    "handle" should {
      "pass correct request" in {
        requestHandlerImpl.handle(
          SignUpRequest(
            //            Option.empty,
            Option("John"),
            Option("Smith"),
            //            Option("johny%"),
            Option("johny"),
            Option("poh"),
            Option("911")
          )
        )(mapper) should equal(Right(SignUpResponse(OK, SignUpRequest(
          Option("John"),
          Option("Smith"),
          Option("johny"),
          Option("poh"),
          Option("911")
        ))))
      }

      "deny not validated request, with empty fields" in {
        requestHandlerImpl.handle(
          SignUpRequest(
            Option.empty,
            Option("Smith"),
            Option("*johny*%"),
            Option("parole"),
            Option("911")
          )
        )(mapper) should equal(Left(ValidationError(Map("name" -> EmptyStringError, "login" -> WrongSymbolStringError)
        )))
      }
    }
  }
}
