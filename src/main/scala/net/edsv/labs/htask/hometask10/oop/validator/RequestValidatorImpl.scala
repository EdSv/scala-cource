package net.edsv.labs.htask.hometask10.oop.validator

import net.edsv.labs.htask.hometask10.oop.errors
import net.edsv.labs.htask.hometask10.oop.errors.ValidationError
import net.edsv.labs.htask.hometask10.oop.request.SignUpRequest

import scala.collection.immutable

trait RequestValidatorImpl extends RequestValidator[SignUpRequest] {

  override def validate(request: SignUpRequest): Either[errors.Error, SignUpRequest] = {
    val errorOrUnits: immutable.Seq[Either[ValidationError, Unit]] = List(
      ValidatorUtil.validateStringEmptyParam(RequestValidator.Name, request.name),
      ValidatorUtil.validateStringEmptyParam(RequestValidator.Surname, request.surname),
      ValidatorUtil.validateStringEmptyParam(RequestValidator.Login, request.login),
      ValidatorUtil.validateStringEmptyParam(RequestValidator.pass, request.pass),
      ValidatorUtil.validateStringEmptyParam(RequestValidator.Msisdn, request.msisdn),
      ValidatorUtil.validateStringSpecialSymbolParam(RequestValidator.Name, request.name),
      ValidatorUtil.validateStringSpecialSymbolParam(RequestValidator.Surname, request.surname),
      ValidatorUtil.validateStringSpecialSymbolParam(RequestValidator.Login, request.login)
    )

    val either = errorOrUnits
      .fold(Right(())) {
        case (Left(v1: ValidationError), Left(v2: ValidationError)) => Left[ValidationError, Unit](v1 + v2)
        case (Left(v1: ValidationError), Right(())) => Left(v1)
        case (Right(_: Unit), Left(v2: ValidationError)) => Left(v2)
        case (Right(_: Unit), Right(_: Unit)) => Right(())
      }

    either.map(_ => request)

  }

}
