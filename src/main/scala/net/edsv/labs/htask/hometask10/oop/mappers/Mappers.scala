package net.edsv.labs.htask.hometask10.oop.mappers

import net.edsv.labs.htask.hometask10.oop.dto.SignUpDto
import net.edsv.labs.htask.hometask10.oop.request.SignUpRequest

// here you can assign your implicit mapper function  implement this trait with your logic
trait Mappers {

  // implement this
  implicit val signUpRequestMapper: SignUpRequest => SignUpDto = (req: SignUpRequest) => {
    SignUpDto(req.name.get, req.surname.get, req.login.get, req.pass.get, req.msisdn.get)
  }


}