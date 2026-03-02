package com.gomaruart.domain.repositories

import com.gomaruart.domain.models.Obra

interface ObraRepository {
    fun findAll(): List<Obra>
    fun findById(id: Int): Obra?
    fun findByCategoria(idCategoria: Int): List<Obra>
    fun create(obra: Obra, idAdmin: Int): Obra?
    fun update(id: Int, obra: Obra): Obra?
    fun delete(id: Int): Boolean
}