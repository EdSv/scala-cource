package net.edsv.labs.htask.hometask10.oop.exception

// Here you can declare your exceptions
case class MapperException(errMsg: String) extends RuntimeException(errMsg)
