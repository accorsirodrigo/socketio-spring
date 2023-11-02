package dev.rodrigoaccorsi.socketiospring

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.listener.ConnectListener
import com.corundumstudio.socketio.listener.DataListener
import com.corundumstudio.socketio.listener.DisconnectListener
import com.corundumstudio.socketio.protocol.Packet
import com.corundumstudio.socketio.protocol.PacketType
import dev.rodrigoaccorsi.socketiospring.entity.Message
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SocketModule(private val server: SocketIOServer) {

    init {
        server.addConnectListener(onConnected())
        server.addDisconnectListener(onDisconnected())
        server.addEventListener("send_message", Message::class.java, onChatReceived())
    }

    private fun onChatReceived(): DataListener<Message> {
        return DataListener<Message> { senderClient: SocketIOClient, data: Message, ackSender: AckRequest? ->
            log.info(data.toString())
            senderClient.namespace.getRoomOperations(data.room).sendEvent("get_message", data.message)
            senderClient.namespace.getRoomOperations(data.room).send( Packet(PacketType.ACK) )

            senderClient.namespace.getRoomOperations(data.room).send( Packet(PacketType.MESSAGE).let {
                it.setData(data.message)
                it
            } )

            senderClient.namespace.getRoomOperations(data.room).send( Packet(PacketType.PONG) )
        }
    }

    private fun onConnected(): ConnectListener {
        return ConnectListener { client: SocketIOClient ->
            val room = client.handshakeData.getSingleUrlParam("room")
            client.joinRoom(room)
            log.info("Socket ID[{}]  Connected to socket", client.sessionId.toString())
        }
    }

    private fun onDisconnected(): DisconnectListener {
        return DisconnectListener { client: SocketIOClient ->
            log.info(
                "Client[{}] - Disconnected from socket",
                client.sessionId.toString()
            )
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }
}