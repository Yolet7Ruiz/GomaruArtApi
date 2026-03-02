package com.gomaruart.domain.services

import com.gomaruart.domain.models.Obra
import com.gomaruart.domain.models.ObraRequest
import com.gomaruart.domain.models.ObraResponse
import com.gomaruart.domain.repositories.ObraRepository

class ObraService(private val obraRepository: ObraRepository) {

    fun findAll(): List<ObraResponse> {
        return obraRepository.findAll().map { mapToResponse(it) }
    }

    fun findById(id: Int): ObraResponse? {
        return obraRepository.findById(id)?.let { mapToResponse(it) }
    }

    fun findByCategoria(idCategoria: Int): List<ObraResponse> {
        return obraRepository.findByCategoria(idCategoria).map { mapToResponse(it) }
    }

    fun create(request: ObraRequest, idAdmin: Int): ObraResponse? {
        val obra = Obra(
            titulo = request.titulo,
            descripcion = request.descripcion,
            precio = request.precio,
            imagen_url = request.imagen_url,
            tecnica_materiales = request.tecnica_materiales,
            id_categoria = request.id_categoria,
            id_admin = idAdmin
        )
        return obraRepository.create(obra, idAdmin)?.let { mapToResponse(it) }
    }

    fun update(id: Int, request: ObraRequest): ObraResponse? {
        val obraExistente = obraRepository.findById(id) ?: return null
        val obraActualizada = obraExistente.copy(
            titulo = request.titulo,
            descripcion = request.descripcion,
            precio = request.precio,
            imagen_url = request.imagen_url,
            tecnica_materiales = request.tecnica_materiales,
            id_categoria = request.id_categoria
        )
        return obraRepository.update(id, obraActualizada)?.let { mapToResponse(it) }
    }

    fun delete(id: Int): Boolean = obraRepository.delete(id)

    private fun mapToResponse(obra: Obra): ObraResponse {
        val categoria = when (obra.id_categoria) {
            1 -> "ANIME"
            2 -> "REALISMO"
            else -> "TODO"
        }
        return ObraResponse(
            id_obra = obra.id_obra,
            titulo = obra.titulo,
            descripcion = obra.descripcion,
            precio = obra.precio,
            imagen_url = obra.imagen_url,
            tecnica_materiales = obra.tecnica_materiales,
            categoria = categoria,
            fecha_creacion = obra.fecha_creacion
        )
    }
}