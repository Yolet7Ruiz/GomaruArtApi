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

        // 1. OBTENER TODAS
        get {
            try {
                val obras = obraService.findAll()
                call.respond(HttpStatusCode.OK, obras)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }

        // 2. CREAR (POST)
        post {
            try {
                val idAdmin = 1 // Hardcoded para desarrollo
                val request = call.receive<ObraRequest>()
                val obra = obraService.create(request, idAdmin)
                if (obra != null) call.respond(HttpStatusCode.Created, obra)
                else call.respond(HttpStatusCode.BadRequest, ErrorResponse("ERROR", "No se pudo crear"))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }

        // 3. ACTUALIZAR (PUT)
        put("/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_ID", "ID inválido"))
                    return@put
                }
                val request = call.receive<ObraRequest>()
                val obra = obraService.update(id, request)
                if (obra != null) call.respond(HttpStatusCode.OK, obra)
                else call.respond(HttpStatusCode.NotFound, ErrorResponse("NOT_FOUND", "No encontrada"))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }

        // 4. ELIMINAR (DELETE)
        delete("/{id}") {
            try {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("INVALID_ID", "ID inválido"))
                    return@delete
                }
                val deleted = obraService.delete(id)
                if (deleted) call.respond(HttpStatusCode.OK, mapOf("message" to "Obra eliminada"))
                else call.respond(HttpStatusCode.NotFound, ErrorResponse("NOT_FOUND", "No encontrada"))
            } catch (e: Exception) {
                e.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, ErrorResponse("ERROR", e.message ?: "Error"))
            }
        }
    }
}