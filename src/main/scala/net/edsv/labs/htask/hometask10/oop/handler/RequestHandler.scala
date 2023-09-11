package net.edsv.labs.htask.hometask10.oop.handler

import net.edsv.labs.htask.hometask10.oop.mappers.Mapper
import net.edsv.labs.htask.hometask10.oop.processor.Processor
import net.edsv.labs.htask.hometask10.oop.validator.RequestValidator
import net.edsv.labs.htask.hometask10.oop.errors.Error


// implement this abstraction use self type trait mixin to implement validate -> map -> process logic
// you can't change signature or self type abstraction
trait RequestHandler[R, DTO, RESP] {

  this: RequestValidator[R] with Mapper[R, DTO] with Processor[DTO, RESP] =>

  def handle(request: R)(implicit mapperFunc: R => DTO): Either[Error, RESP] = ???

}


