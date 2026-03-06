package com.gomaruart.domain.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Obra(
    val id_obra: Int = 0,
    val titulo: String,
    val descripcion: String? = null,
    val precio: Double,
    val imagen_url: String,
    val tecnica_materiales: String? = null,

    // 🔥 Mapeo para que entienda el JSON que viene de Android
    @SerialName("categoryId")
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

    // 🔥 Mapeo también en el Request para los POST y PUT
    @SerialName("categoryId")
    val id_categoria: Int = 1

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