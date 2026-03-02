package com.gomaruart.data.repositories

import com.gomaruart.config.DatabaseConfig
import com.gomaruart.domain.models.Obra
import com.gomaruart.domain.repositories.ObraRepository
import java.sql.ResultSet

class ObraRepositoryImpl : ObraRepository {

    override fun findAll(): List<Obra> {
        val obras = mutableListOf<Obra>()
        val sql = """
            SELECT o.*, c.nombre_categoria 
            FROM obras o
            JOIN categorias c ON o.id_categoria = c.id_categoria
            WHERE o.activo = true
            ORDER BY o.fecha_creacion DESC
        """.trimIndent()

        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    obras.add(mapResultSetToObra(rs))
                }
            }
        }
        return obras
    }

    override fun findById(id: Int): Obra? {
        val sql = "SELECT * FROM obras WHERE id_obra = ? AND activo = true"

        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                val rs = stmt.executeQuery()
                if (rs.next()) {
                    return mapResultSetToObra(rs)
                }
            }
        }
        return null
    }

    override fun findByCategoria(idCategoria: Int): List<Obra> {
        val obras = mutableListOf<Obra>()
        val sql = "SELECT * FROM obras WHERE id_categoria = ? AND activo = true"

        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, idCategoria)
                val rs = stmt.executeQuery()
                while (rs.next()) {
                    obras.add(mapResultSetToObra(rs))
                }
            }
        }
        return obras
    }

    override fun create(obra: Obra, idAdmin: Int): Obra? {
        val sql = """
            INSERT INTO obras (titulo, descripcion, precio, imagen_url, tecnica_materiales, id_categoria, id_admin) 
            VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_obra
        """.trimIndent()

        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, obra.titulo)
                stmt.setString(2, obra.descripcion)
                stmt.setDouble(3, obra.precio)
                stmt.setString(4, obra.imagen_url)
                stmt.setString(5, obra.tecnica_materiales)
                stmt.setInt(6, obra.id_categoria)
                stmt.setInt(7, idAdmin)

                val rs = stmt.executeQuery()
                if (rs.next()) {
                    val id = rs.getInt(1)
                    return findById(id)
                }
            }
        }
        return null
    }

    override fun update(id: Int, obra: Obra): Obra? {
        val sql = """
            UPDATE obras 
            SET titulo = ?, descripcion = ?, precio = ?, imagen_url = ?, 
                tecnica_materiales = ?, id_categoria = ?
            WHERE id_obra = ? AND activo = true
        """.trimIndent()

        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setString(1, obra.titulo)
                stmt.setString(2, obra.descripcion)
                stmt.setDouble(3, obra.precio)
                stmt.setString(4, obra.imagen_url)
                stmt.setString(5, obra.tecnica_materiales)
                stmt.setInt(6, obra.id_categoria)
                stmt.setInt(7, id)

                val updated = stmt.executeUpdate()
                return if (updated > 0) findById(id) else null
            }
        }
    }

    override fun delete(id: Int): Boolean {
        val sql = "UPDATE obras SET activo = false WHERE id_obra = ?"

        DatabaseConfig.getConnection().use { conn ->
            conn.prepareStatement(sql).use { stmt ->
                stmt.setInt(1, id)
                return stmt.executeUpdate() > 0
            }
        }
    }

    private fun mapResultSetToObra(rs: ResultSet): Obra = Obra(
        id_obra = rs.getInt("id_obra"),
        titulo = rs.getString("titulo"),
        descripcion = rs.getString("descripcion"),
        precio = rs.getDouble("precio"),
        imagen_url = rs.getString("imagen_url"),
        tecnica_materiales = rs.getString("tecnica_materiales"),
        id_categoria = rs.getInt("id_categoria"),
        id_admin = rs.getInt("id_admin"),
        fecha_creacion = rs.getString("fecha_creacion"),
        activo = rs.getBoolean("activo")
    )
}