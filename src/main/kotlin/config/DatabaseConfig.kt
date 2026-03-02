package com.gomaruart.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection

object DatabaseConfig {
    private var dataSource: HikariDataSource? = null

    fun init() {
        if (dataSource == null) {
            val config = HikariConfig().apply {
                jdbcUrl = "jdbc:postgresql://localhost:5432/gomaruart_db"
                username = "postgres"
                password = "gomaru"
                maximumPoolSize = 5
            }
            dataSource = HikariDataSource(config)
            println("Base de datos inicializada")
        }
    }

    fun getConnection(): Connection {
        if (dataSource == null) init()
        return dataSource!!.connection
    }
}