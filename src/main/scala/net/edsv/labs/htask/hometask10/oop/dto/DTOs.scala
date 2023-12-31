package net.edsv.labs.htask.hometask10.oop.dto

import net.edsv.labs.htask.hometask10.oop.request.Experience


/// DTOs
case class SignUpDto(
                      name: String,
                      surname: String,
                      login: String,
                      secret: String,
                      msisdn: String
                    )

case class SignInDto(
                      login: String,
                      secret: String
                    )

case class WorkInfoInDto(
                          login: String,
                          workExperience: List[Experience],
                          certificates: List[String]
                        )


