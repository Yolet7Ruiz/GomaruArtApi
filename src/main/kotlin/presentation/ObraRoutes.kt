package com.gomaruart.presentation.routes

import com.gomaruart.data.repositories.ObraRepositoryImpl
import com.gomaruart.domain.models.ErrorResponse
import com.gomaruart.domain.models.ObraRequest
import com.gomaruart.domain.services.ObraService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.obraRoutes() {
    val obraRepository = ObraRepositoryImpl()
    val obraService = ObraService(obraRepository)

    route("/obras") {

        // GET /obras - Listar todas
        get {
            try {
                val obras = obraService.findAll()
                call.respond(HttpStatusCode.OK, obras)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }

        // GET /obras/categoria/{id} - Filtrar por categoría
        get("/categoria/{id}") {
            try {
                val idCategoria = call.parameters["id"]?.toIntOrNull()
                if (idCategoria == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_ID", "ID inválido"))
                    return@get
                }
                val obras = obraService.findByCategoria(idCategoria)
                call.respond(HttpStatusCode.OK, obras)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }

        // GET /obras/{id} - Obtener una obra
        get("/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_ID", "ID inválido"))
                    return@get
                }
                val obra = obraService.findById(id)
                if (obra != null) {
                    call.respond(HttpStatusCode.OK, obra)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("NOT_FOUND", "Obra no encontrada"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }

        // POST /obras - Crear obra
        post {
            try {
                // Obtener el token del header Authorization
                val authHeader = call.request.headers["Authorization"]
                if (authHeader.isNullOrBlank()) {
                    call.respond(HttpStatusCode.Unauthorized, ErrorResponse("UNAUTHORIZED", "Se requiere token de autenticación"))
                    return@post
                }

                // Como solo hay un admin, usamos ID 1
                // En un sistema real, aquí decodificarías el JWT
                val idAdmin = 1

                val request = call.receive<ObraRequest>()
                val obra = obraService.create(request, idAdmin)

                if (obra != null) {
                    call.respond(HttpStatusCode.Created, obra)
                } else {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("ERROR", "No se pudo crear"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }

        // PUT /obras/{id} - Actualizar
        put("/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_ID", "ID inválido"))
                    return@put
                }

                val request = call.receive<ObraRequest>()
                val obra = obraService.update(id, request)
                if (obra != null) {
                    call.respond(HttpStatusCode.OK, obra)
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("NOT_FOUND", "Obra no encontrada"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }

        // DELETE /obras/{id} - Eliminar (soft delete)
        delete("/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_ID", "ID inválido"))
                    return@delete
                }

                val deleted = obraService.delete(id)
                if (deleted) {
                    call.respond(HttpStatusCode.OK, mapOf("message" to "Obra eliminada"))
                } else {
                    call.respond(HttpStatusCode.NotFound, ErrorResponse("NOT_FOUND", "Obra no encontrada"))
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }
    }
}