package net.edsv.labs.htask.hometask10.oop.errors


// here you can declare your errors
sealed trait Error {

  def errorMessage: String
}


// mapper errors
case object LackMappedParamError extends Error {
  override val errorMessage: String = "lackMapperParam"
}

case object MapperError extends Error {
  override val errorMessage: String = "mapperError"
}

case object SignUpError extends Error {
  override val errorMessage: String = "SignUpError"
}

// validation Errors
case object EmptyStringError extends Error {
  override val errorMessage: String = "emptyError"
}

case object WrongSymbolStringError extends Error {
  override val errorMessage: String = "specialSymbolNotAcceptedError"
}

// base trait for errors with merging effect
trait CombinedError[T <: CombinedError[T]] extends Error {

  def errors: Map[String, Error]

  def +(error: T): T

}

//
case class ValidationError(errors: Map[String, Error]) extends CombinedError[ValidationError] {
  require(errors.nonEmpty)

  private val spliterator = ", "

  def this(param: String, error: Error) = this(Map(param -> error))

  // TODO

  /**
   * implement this
   * This method has to merge this validationError with @param error
   *
   * @return new merged combined Validation Error
   */
  override def +(error: ValidationError): ValidationError = {
    ValidationError(errors ++ error.errors)
  }

  // TODO
  /**
   * implement this
   * errorMessage using next pattern: -  errorMessage : [ field1,field2,....], errorMessage2: [field3,field4]
   *
   * @return merged combined Validation Error
   */
  override val errorMessage: String = {
    val stringToTuples: Map[String, List[(String, Error)]] = errors.toList
      .groupBy(text => text._2.errorMessage)

    stringToTuples.map(c => (c._1, c._2.map(e => e._1).reduce((f1, f2) => f1 + spliterator + f2)))
      .map { case (x, y) => s"$x : [$y]" }
      .foldRight("")((f1, f2) => f1 + spliterator + f2)
  }

}