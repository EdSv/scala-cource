package net.edsv.labs.htask.hometask10.oop.response

import net.edsv.labs.htask.hometask10.oop.dto.SignUpDto
import net.edsv.labs.htask.hometask10.oop.processor.Status


case class SignUpResponse(status: Status, dto: SignUpDto)

case class SignInResponse()

case class WorkInfoResponse()
