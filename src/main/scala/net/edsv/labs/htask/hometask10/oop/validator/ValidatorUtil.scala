package net.edsv.labs.htask.hometask10.oop.validator

import net.edsv.labs.htask.hometask10.oop.errors.{EmptyStringError, ValidationError, WrongSymbolStringError}

// Here you can declare all supplementary method for validation with style below(or use your own signature and return type
// if you want)
object ValidatorUtil {

  def validateStringEmptyParam(paramName: String, maybeStringEmpty: Option[String]): Either[ValidationError, Unit] = {
    maybeStringEmpty.map(_ => Right(())).getOrElse(Left(new ValidationError(paramName, EmptyStringError)))
  }

  def validateStringSpecialSymbolParam(paramName: String, maybeStringEmpty: Option[String]): Either[ValidationError, Unit] = {
    val regex = "[~!@#$^%&*\\(\\)_+={}\\[\\]|;:\"'<,>.?`/\\\\-]".r
    maybeStringEmpty.map(s => regex.pattern.asPredicate().test(s))
      .map {
        case true => Left(new ValidationError(paramName, WrongSymbolStringError))
        case false => Right(())
      }
      .getOrElse(Right())

  }
  /// ........
}
