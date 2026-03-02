package com.gomaruart.domain.repositories

import com.gomaruart.domain.models.Usuario

interface
UsuarioRepository {
    fun findByUsername(username: String): Usuario?
    fun validateUser(username: String, password: String): Boolean
    fun createUser(username: String, password: String): Usuario?
}