package net.edsv.labs.htask.hometask10.oop.processor

import net.edsv.labs.htask.hometask10.oop.dto.SignUpDto
import net.edsv.labs.htask.hometask10.oop.errors
import net.edsv.labs.htask.hometask10.oop.errors.SignUpError
import net.edsv.labs.htask.hometask10.oop.response.SignUpResponse
import net.edsv.labs.htask.hometask10.oop.services.{HashService, LoginService}

import scala.util.{Failure, Success, Try}


trait ProcessorImpl extends Processor[SignUpDto, SignUpResponse] {

  def loginService: LoginService

  def hashService: HashService

  override def process(in: SignUpDto): Either[errors.Error, SignUpResponse] = {
    //    Right(SignUpResponse(OK, in))

    val checkLogin = Try(loginService.checkUniqueness(in.login))
    val checkHash = hashService.hash(in.secret)
    (checkLogin, checkHash) match {
      case (Success(true), _) => Right(SignUpResponse(OK, in))
      case (Success(false), _) => Right(SignUpResponse(OK, in))
      case (Failure(_: Throwable), _) => Left(SignUpError)
    }
  }

}