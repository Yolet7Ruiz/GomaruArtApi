package com.gomaruart.presentation.routes

import com.gomaruart.data.repositories.UsuarioRepositoryImpl
import com.gomaruart.domain.models.ErrorResponse
import com.gomaruart.domain.models.LoginRequest
import com.gomaruart.domain.services.AuthService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory

fun Route.authRoutes() {
    val usuarioRepository = UsuarioRepositoryImpl()
    val authService = AuthService(usuarioRepository)
    val logger = LoggerFactory.getLogger("AuthRoutes")

    route("/auth") {

        // POST /auth/login
        post("/login") {
            try {
                logger.info("Petición de login recibida")

                val loginRequest = try {
                    call.receive<LoginRequest>()
                } catch (e: Exception) {
                    logger.error("Error al parsear JSON: ${e.message}")
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("INVALID_JSON", "Formato JSON inválido")
                    )
                    return@post
                }

                val response = authService.login(loginRequest)

                if (response.success) {
                    logger.info("Login exitoso: ${loginRequest.username}")
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    logger.warn("Login fallido: ${loginRequest.username}")
                    call.respond(HttpStatusCode.Unauthorized, response)
                }

            } catch (e: Exception) {
                logger.error("Error inesperado: ${e.message}")
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse("SERVER_ERROR", "Error interno del servidor")
                )
            }
        }

        // GET /auth/test (para probar)
        get("/test") {
            call.respond(mapOf(
                "message" to "Auth endpoints funcionando",
                "status" to "OK"
            ))
        }
    }
}