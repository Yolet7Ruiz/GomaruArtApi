package com.gomaruart

import com.gomaruart.config.DatabaseConfig
import com.gomaruart.presentation.routes.authRoutes
import com.gomaruart.presentation.routes.obraRoutes  // ← IMPORTAR
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("API funcionando")
        }

        get("/test-db") {
            try {
                val connection = DatabaseConfig.getConnection()
                connection.use { conn ->
                    val rs = conn.createStatement().executeQuery("SELECT 1")
                    rs.next()
                    call.respondText("Conexión OK: ${rs.getInt(1)}")
                }
            } catch (e: Exception) {
                call.respondText("Error: ${e.message}")
            }
        }

        authRoutes()
        obraRoutes()
    }
}