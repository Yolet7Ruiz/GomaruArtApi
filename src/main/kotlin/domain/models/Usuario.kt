package com.gomaruart.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    val id: Int = 0,
    val username: String,
    val passwordHash: String,
    val createdAt: String = ""
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val username: String? = null
)

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String
)