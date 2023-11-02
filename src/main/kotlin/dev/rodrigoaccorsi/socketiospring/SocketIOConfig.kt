package dev.rodrigoaccorsi.socketiospring

import com.corundumstudio.socketio.SocketIOServer
import com.corundumstudio.socketio.annotation.SpringAnnotationScanner
import com.corundumstudio.socketio.protocol.JacksonJsonSupport
import com.corundumstudio.socketio.protocol.JsonSupport
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.apache.catalina.mapper.Mapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class SocketIOConfig(
    @Value("\${wss.server.host}")
    private val host: String,
    @Value("\${wss.server.port}")
    private val port: Int,
) {
    @Bean
    fun socketIOServer(): SocketIOServer{
        val config = com.corundumstudio.socketio.Configuration()
        config.hostname = host
        config.port = port
        config.jsonSupport = JacksonJsonSupport(KotlinModule.Builder().build())
        return SocketIOServer(config)
    }

    @Bean
    fun springAnnotationScanner(ssrv: SocketIOServer): SpringAnnotationScanner {
        return SpringAnnotationScanner(ssrv)
    }

    @Bean
    fun jacksonObjectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.registerKotlinModule()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE, true)
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        mapper.registerModule(JavaTimeModule())
        return mapper
    }

}