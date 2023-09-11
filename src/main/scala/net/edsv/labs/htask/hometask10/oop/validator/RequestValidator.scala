package net.edsv.labs.htask.hometask10.oop.validator

import net.edsv.labs.htask.hometask10.oop.errors.Error


// here you can implement sub-traits for validation purpose


trait RequestValidator[R] {

  def validate(request: R): Either[Error, R]

}

object RequestValidator {

   val Name    = "name"
   val Surname = "surname"
   val Login   = "login"
   val pass    = "pass"
   val Msisdn  = "msisdn"
}
