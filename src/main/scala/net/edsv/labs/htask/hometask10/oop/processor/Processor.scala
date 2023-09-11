package net.edsv.labs.htask.hometask10.oop.processor

import net.edsv.labs.htask.hometask10.oop.errors.Error

trait Processor[DTO, RESP] {

  def process(in: DTO): Either[Error, RESP]

}


//  Response status Always wil be ok for just now
trait Status

case object OK extends Status
