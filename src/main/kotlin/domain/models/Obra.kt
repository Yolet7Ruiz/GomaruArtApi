package com.gomaruart.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Obra(
    val id_obra: Int = 0,
    val titulo: String,
    val descripcion: String? = null,
    val precio: Double,
    val imagen_url: String,
    val tecnica_materiales: String? = null,
    val id_categoria: Int,
    val id_admin: Int,
    val fecha_creacion: String = "",
    val activo: Boolean = true
)

@Serializable
data class ObraRequest(
    val titulo: String,
    val descripcion: String? = null,
    val precio: Double,
    val imagen_url: String,
    val tecnica_materiales: String? = null,
    val id_categoria: Int
)

@Serializable
data class ObraResponse(
    val id_obra: Int,
    val titulo: String,
    val descripcion: String?,
    val precio: Double,
    val imagen_url: String,
    val tecnica_materiales: String?,
    val categoria: String,
    val fecha_creacion: String
)