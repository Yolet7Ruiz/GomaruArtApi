package com.gomaruart

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json // Importante para configurar el bloque json

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {

            prettyPrint = true          // Hace que el JSON sea legible en logs
            isLenient = true            // Es más permisivo con formatos de texto
            ignoreUnknownKeys = true    // LA MAGIA: ignora campos extra como 'id_obra'
            encodeDefaults = true       // Incluye valores por defecto en el JSON
        })
    }

    routing {
        get("/json/kotlinx-serialization") {
            call.respond(mapOf("hello" to "world"))
        }
    }
}