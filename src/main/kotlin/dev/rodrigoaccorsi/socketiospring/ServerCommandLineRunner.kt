package dev.rodrigoaccorsi.socketiospring

import com.corundumstudio.socketio.SocketIOServer
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class ServerCommandLineRunner(
    private val server: SocketIOServer
): CommandLineRunner {
    override fun run(vararg args: String?) {
        server.start()
    }
}