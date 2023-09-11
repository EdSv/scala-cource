package net.edsv.labs.htask.hometask10.oop.mappers

import net.edsv.labs.htask.hometask10.oop.dto.SignUpDto
import net.edsv.labs.htask.hometask10.oop.errors
import net.edsv.labs.htask.hometask10.oop.errors.MapperError
import net.edsv.labs.htask.hometask10.oop.request.SignUpRequest

import scala.util.Try

trait MapperImpl extends Mapper[SignUpRequest, SignUpDto] {

  override def map(request: SignUpRequest)(implicit defaultMapper: SignUpRequest => SignUpDto): Either[errors.Error, SignUpDto] =
    Try(
      defaultMapper.apply(request)
    ).toEither.left.map(_ => MapperError)

}
