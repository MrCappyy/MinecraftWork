package me.mrcappy.auctionhouse.utils

import me.mrcappy.auctionhouse.Main
import java.sql.Connection
import java.sql.DriverManager

object DatabaseUtils {
    private lateinit var connection: Connection

    fun connect() {
        val dbType = Main.instance.config.getString("database.type") ?: "SQLite"
        val dbUrl = if (dbType.equals("MySQL", true)) {
            val host = Main.instance.config.getString("database.host")
            val port = Main.instance.config.getInt("database.port")
            val name = Main.instance.config.getString("database.name")
            val user = Main.instance.config.getString("database.user")
            val password = Main.instance.config.getString("database.password")
            "jdbc:mysql://$host:$port/$name?user=$user&password=$password"
        } else {
            "jdbc:sqlite:${Main.instance.dataFolder}/auctionhouse.db"
        }

        connection = DriverManager.getConnection(dbUrl)
        createTables()
        println("Database connected successfully!")
    }

    private fun createTables() {
        val statement = connection.createStatement()
        statement.executeUpdate("""
            CREATE TABLE IF NOT EXISTS auctions (
                id TEXT PRIMARY KEY,
                seller TEXT,
                item BLOB,
                starting_price REAL,
                current_bid REAL,
                buy_now_price REAL,
                end_time INTEGER,
                highest_bidder TEXT
            )
        """.trimIndent())
        statement.close()
    }

    fun disconnect() {
        if (::connection.isInitialized && !connection.isClosed) {
            connection.close()
            println("Database connection closed.")
        }
    }

    fun getConnection(): Connection = connection
}
