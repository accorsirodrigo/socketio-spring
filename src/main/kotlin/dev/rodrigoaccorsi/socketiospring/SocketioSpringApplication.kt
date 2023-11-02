package dev.rodrigoaccorsi.socketiospring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SocketioSpringApplication

fun main(args: Array<String>) {
	runApplication<SocketioSpringApplication>(*args)
}
