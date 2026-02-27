package com.gomaruart.domain.services

import com.gomaruart.domain.models.LoginRequest
import com.gomaruart.domain.models.LoginResponse
import com.gomaruart.domain.repositories.UsuarioRepository
import java.util.*

class AuthService(private val usuarioRepository: UsuarioRepository) {

    fun login(loginRequest: LoginRequest): LoginResponse {
        // Validar que no vengan vacíos
        if (loginRequest.username.isBlank() || loginRequest.password.isBlank()) {
            return LoginResponse(
                success = false,
                message = "Usuario y contraseña son requeridos",
                token = null,
                username = null
            )
        }

        // Validar credenciales
        val isValid = usuarioRepository.validateUser(
            loginRequest.username,
            loginRequest.password
        )

        return if (isValid) {
            // Generar token simple
            val token = Base64.getEncoder().encodeToString(
                "${loginRequest.username}:${System.currentTimeMillis()}".toByteArray()
            )

            LoginResponse(
                success = true,
                message = "Login exitoso",
                token = token,
                username = loginRequest.username
            )
        } else {
            LoginResponse(
                success = false,
                message = "Usuario o contraseña incorrectos",
                token = null,
                username = null
            )
        }
    }

    fun register(username: String, password: String): LoginResponse {
        if (password.length < 6) {
            return LoginResponse(
                success = false,
                message = "La contraseña debe tener al menos 6 caracteres",
                token = null,
                username = null
            )
        }

        val nuevoUsuario = usuarioRepository.createUser(username, password)

        return if (nuevoUsuario != null) {
            LoginResponse(
                success = true,
                message = "Usuario registrado exitosamente",
                token = null,
                username = username
            )
        } else {
            LoginResponse(
                success = false,
                message = "El usuario ya existe o error al crear",
                token = null,
                username = null
            )
        }
    }
}