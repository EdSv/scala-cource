package net.edsv.labs.htask.hometask10.oop.handler

import net.edsv.labs.htask.hometask10.oop.dto.SignUpDto
import net.edsv.labs.htask.hometask10.oop.mappers.MapperImpl
import net.edsv.labs.htask.hometask10.oop.processor.ProcessorImpl
import net.edsv.labs.htask.hometask10.oop.request.SignUpRequest
import net.edsv.labs.htask.hometask10.oop.response.SignUpResponse
import net.edsv.labs.htask.hometask10.oop.services.{HashService, LoginService}
import net.edsv.labs.htask.hometask10.oop.validator.RequestValidatorImpl

// implement this abstraction use self type trait mixin to implement validate -> map -> process logic
class RequestHandlerImpl(var loginService: LoginService,
                         var hashService: HashService) extends RequestHandler[SignUpRequest, SignUpDto, SignUpResponse] with RequestValidatorImpl with ProcessorImpl with MapperImpl {

  override def handle(request: SignUpRequest)(implicit mapperFunc: SignUpRequest => SignUpDto) = {
    validate(request) map {mapperFunc} flatMap {process}
  }

}
