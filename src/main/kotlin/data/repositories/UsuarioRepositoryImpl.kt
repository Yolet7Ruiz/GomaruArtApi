package com.gomaruart.data.repositories

import com.gomaruart.config.DatabaseConfig
import com.galeria.app.config.SecurityConfig
import com.gomaruart.domain.models.Usuario
import com.gomaruart.domain.repositories.UsuarioRepository
import java.sql.ResultSet

class UsuarioRepositoryImpl : UsuarioRepository {

    override fun findByUsername(username: String): Usuario? {
        val sql = "SELECT * FROM usuarios WHERE username = ?"

        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, username)

                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return mapResultSetToUsuario(rs)
                }
            }
        }
        return null
    }

    override fun validateUser(username: String, password: String): Boolean {
        val usuario = findByUsername(username)
        return if (usuario != null) {
            SecurityConfig.checkPassword(password, usuario.passwordHash)
        } else {
            false
        }
    }

    override fun createUser(username: String, password: String): Usuario? {
        if (findByUsername(username) != null) {
            return null
        }

        val hash = SecurityConfig.hashPassword(password)
        val sql = "INSERT INTO usuarios (username, password_hash) VALUES (?, ?) RETURNING id"

        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, username)
                stmt.setString(2, hash)

                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val id = rs.getInt(1)
                    return Usuario(id, username, hash, "")  // ← String vacío
                }
            }
        }
        return null
    }

    private fun mapResultSetToUsuario(rs: ResultSet): Usuario = Usuario(
        id = rs.getInt("id"),
        username = rs.getString("username"),
        passwordHash = rs.getString("password_hash"),
        createdAt = rs.getString("created_at")  // ← Directo como String
    )
}