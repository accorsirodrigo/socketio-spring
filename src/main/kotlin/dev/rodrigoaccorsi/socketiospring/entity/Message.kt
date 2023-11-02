package dev.rodrigoaccorsi.socketiospring.entity

data class Message(
     val type: MessageType?,
     val message: String?,
     val room: String
)
