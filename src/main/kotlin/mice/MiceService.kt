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

data class Mice(
        var id: String = "", // 🔹 Asegura que `id` nunca sea null
        var name: String = "",
        val dpi: Int = 0,
        val buttons: Int = 0,
        val weight: String = "",
        val wireless: Boolean = false,
        val price: Double = 0.0,
        val imageUrl: String = ""
)

object MiceService {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)
    /*Enumera los atributos*/
    private fun rowToMice(row: Row) =
            Mice(
                    id = row.string("ID"),
                    name = row.string("NAME"),
                    dpi = row.int("DPI"),
                    buttons = row.int("BUTTONS"),
                    weight = row.string("WEIGHT"),
                    wireless = row.boolean("WIRELESS"),
                    price = row.double("PRICE"),
                    imageUrl = row.stringOrNull("IMAGE_URL") ?: "" // 🔹 Agregado
            )

    fun selectAll(): List<Mice> {
        val qry =
                queryOf(
                                """
                    SELECT ID, NAME, DPI, BUTTONS, WEIGHT, WIRELESS, PRICE, IMAGE_URL
                    FROM MICE
                """.trimIndent()
                        )
                        .map(::rowToMice)
                        .asList

        val result: List<Mice>
        sessionOf(HikariCP.dataSource()).use { conn -> result = conn.run(qry) }
        return result
    }

    fun selectById(id: String): Mice {
        val qry =
                queryOf(
                                """
                SELECT ID, NAME, DPI, BUTTONS, WEIGHT, WIRELESS, PRICE, IMAGE_URL
                FROM MICE
                WHERE ID = ?
                """.trimIndent(),
                                id
                        )
                        .map(::rowToMice)
                        .asSingle

        val result: Mice
        sessionOf(HikariCP.dataSource()).use { conn ->
            result = conn.run(qry) ?: throw InternalServerErrorResponse("No existe ese elemento")
        }
        return result
    }

    fun insert(mice: Mice): String {
        val id = UUID.randomUUID().toString() // Genera UUID en formato String

        log.info("UUID generado: $id")
        log.info(
                "Datos enviados: name=${mice.name}, dpi=${mice.dpi}, buttons=${mice.buttons}, weight=${mice.weight}, wireless=${mice.wireless}, price=${mice.price}, imageUrl=${mice.imageUrl}"
        )

        val qry =
                queryOf(
                        """
            INSERT INTO MICE (ID, NAME, DPI, BUTTONS, WEIGHT, WIRELESS, PRICE, IMAGE_URL)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
                        id,
                        mice.name,
                        mice.dpi,
                        mice.buttons,
                        mice.weight,
                        mice.wireless,
                        mice.price,
                        mice.imageUrl
                )

        var result = "failed"
        sessionOf(HikariCP.dataSource()).use { conn ->
            try {
                val rowsAffected = conn.run(qry.asUpdate)
                if (rowsAffected > 0) {
                    result = id
                    log.info("Mouse insertado con éxito con ID: $result")
                } else {
                    log.error("No se insertó el mouse en la base de datos")
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
                DELETE FROM MICE WHERE ID = ?
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
                log.error("Error al eliminar mouse: ${ex.message}")
                dbErrorHandler(log, ex.message)
            }
        }
        return result
    }

    fun update(mice: Mice): String {
        val qry =
                queryOf(
                        """
                UPDATE MICE SET 
                    NAME = ?, 
                    DPI = ?, 
                    BUTTONS = ?, 
                    WEIGHT = ?, 
                    WIRELESS = ?,
                    PRICE = ?,
                    IMAGE_URL = ?
                WHERE ID = ?
                """.trimIndent(),
                        mice.name,
                        mice.dpi,
                        mice.buttons,
                        mice.weight,
                        mice.wireless,
                        mice.price,
                        mice.imageUrl,
                        mice.id
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
                log.error("Error al actualizar mouse: ${ex.message}")
                dbErrorHandler(log, ex.message)
            }
        }
        return result
    }
}
