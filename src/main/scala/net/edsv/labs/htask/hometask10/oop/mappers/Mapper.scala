package net.edsv.labs.htask.hometask10.oop.mappers

import net.edsv.labs.htask.hometask10.oop.errors.Error


private[oop] trait Mapper[R, DTO] {
  def map(request: R)(implicit defaultMapper: R => DTO): Either[Error, DTO]
}


