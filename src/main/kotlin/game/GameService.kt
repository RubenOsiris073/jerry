package mx.edu.uttt.mice

import io.javalin.http.InternalServerErrorResponse
import java.sql.SQLException
import java.util.UUID
import kotliquery.HikariCP
import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import mx.edu.uttt.Utils.dbErrorHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/*Funciones del CRUD*/

data class Game(
        var id: String = "", // 🔹 Asegura que `id` nunca sea null
        var title: String = "",
        val descripcion: String = "",
        val tiempo: Int = 0,
        val price: Double = 0.0,
        val imageUrl: String = ""
)

object GameService {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    /*Enumera los atributos*/
    private fun rowToGame(row: Row) =
            Game(
                    id = row.string("ID"),
                    title = row.string("TITLE"),
                    descripcion = row.string("DESCRIPCION"),
                    tiempo = row.int("TIEMPO"),
                    price = row.double("PRICE"),
                    imageUrl = row.stringOrNull("IMAGE_URL") ?: "" // 🔹 Agregado
            )

    fun selectAll(): List<Game> {
        val qry =
                queryOf(
                                """
                    SELECT ID, TITLE, DESCRIPCION, TIEMPO, PRICE, IMAGE_URL
                    FROM GAME
                """.trimIndent()
                        )
                        .map(::rowToGame)
                        .asList

        val result: List<Game>
        sessionOf(HikariCP.dataSource()).use { conn -> result = conn.run(qry) }
        return result
    }

    fun selectById(id: String): Game {
        val qry =
                queryOf(
                                """
                SELECT ID, TITLE, DESCRIPCION, TIEMPO, PRICE, IMAGE_URL
                    FROM GAME
                WHERE ID= ?
                """.trimIndent(),
                                id
                        )
                        .map(::rowToGame)
                        .asSingle

        val result: Game
        sessionOf(HikariCP.dataSource()).use { conn ->
            result = conn.run(qry) ?: throw InternalServerErrorResponse("No existe ese elemento")
        }
        return result
    }

    fun insert(Game: Game): String {
        val id = UUID.randomUUID().toString() // Genera UUID en formato String

        log.info("UUID generado: $id")
        log.info(
                "Datos enviados: title=${Game.title}, descripcion=${Game.descripcion}, tiempo=${Game.tiempo}, price=${Game.price}, imageUrl=${Game.imageUrl}"
        )

        val qry =
                queryOf(
                        """
            INSERT INTO Game (ID, TITLE, DESCRIPCION, TIEMPO, PRICE, IMAGE_URL)
            VALUES (?, ?, ?, ?, ?,descripcion ?, ?)
            """.trimIndent(),
                        id,
                        Game.title,
                        Game.descripcion,
                        Game.tiempo,
                        Game.price,
                        Game.imageUrl
                )

        var result = "failed"
        sessionOf(HikariCP.dataSource()).use { conn ->
            try {
                val rowsAffected = conn.run(qry.asUpdate)
                if (rowsAffected > 0) {
                    result = id
                    log.info("Game insertado con éxito con ID: $result")
                } else {
                    log.error("No se insertó el game en la base de datos")
                }
            } catch (ex: SQLException) {
                log.error("Error en la inserción: ${ex.message}")
                dbErrorHandler(log, ex.message)
            }
        }
        return result
    }

    fun delete(id: String): String {
        val qry =
                queryOf(
                        """
                DELETE FROM Game WHERE ID = ?
                """.trimIndent(),
                        id
                )

        var result = "failed"
        sessionOf(HikariCP.dataSource()).use { conn ->
            try {
                result =
                        if (conn.run(qry.asUpdate) > 0) "success"
                        else
                                throw InternalServerErrorResponse(
                                        "No se pudo borrar: ID no encontrado"
                                )
            } catch (ex: SQLException) {
                log.error("Error al eliminar game: ${ex.message}")
                dbErrorHandler(log, ex.message)
            }
        }
        return result
    }

    fun update(Game: Game): String {
        val qry =
                queryOf(
                        """
                UPDATE GAME SET 
                    TITLE = ?, 
                    DESCRIPCION = ?, 
                    TIEMPO= ?, 
                    PRICE = ?,
                    IMAGE_URL = ?
                WHERE ID = ?
                """.trimIndent(),
                        Game.title,
                        Game.descripcion,
                        Game.tiempo,
                        Game.price,
                        Game.imageUrl,
                        Game.id
                )

        var result = "failed"
        sessionOf(HikariCP.dataSource()).use { conn ->
            try {
                val rowsAffected = conn.run(qry.asUpdate)
                result =
                        if (rowsAffected > 0) "success"
                        else
                                throw InternalServerErrorResponse(
                                        "No se pudo actualizar: ID no encontrado"
                                )
            } catch (ex: SQLException) {
                log.error("Error al actualizar game: ${ex.message}")
                dbErrorHandler(log, ex.message)
            }
            return result
        }
    }
}
