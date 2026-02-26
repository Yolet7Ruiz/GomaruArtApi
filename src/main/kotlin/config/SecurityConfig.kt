package com.galeria.app.config

import org.mindrot.jbcrypt.BCrypt

object SecurityConfig {

    // Encriptar contraseña
    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    // Verificar contraseña
    fun checkPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }

    // Para probar (opcional - lo puedes borrar después)
    @JvmStatic
    fun main(args: Array<String>) {
        val password = "admin1234"
        val hash = hashPassword(password)
        println("Contraseña original: $password")
        println("Hash generado: $hash")
        println("Verificación: ${checkPassword(password, hash)}")

        // Probar con el hash de la BD
        val hashFromDB = "\$2a\$10\$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE4lBO1oKp5Fv0JRm"
        println("Verificar con hash BD: ${checkPassword(password, hashFromDB)}")
    }
}