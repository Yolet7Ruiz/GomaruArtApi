package com.gomaruart

import com.gomaruart.config.DatabaseConfig
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    // Inicializar BD
    DatabaseConfig.init()

    // Iniciar servidor Ktor
    embeddedServer(Netty, port = 8080) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    // Tus configuraciones
    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting()
}