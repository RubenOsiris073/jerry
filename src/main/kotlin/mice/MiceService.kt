package mx.edu.uttt.mice

import io.javalin.http.InternalServerErrorResponse
import java.sql.SQLException
import kotliquery.HikariCP
import kotliquery.Row
import kotliquery.queryOf
import kotliquery.sessionOf
import mx.edu.uttt.Utils.dbErrorHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/*Funciones del CRUD*/

data class Mice(
        var id: String?,
        var name: String,
        val dpi: Int,
        val buttons: Int,
        val weight: String,
        val wireless: Boolean,
        val price: Double
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
                    price = row.double("PRICE")
            )

    fun selectAll(): List<Mice> {
        val qry =
                queryOf(
                                """
                    SELECT CAST(ID AS VARCHAR(36)) AS ID, NAME, DPI, BUTTONS, WEIGHT, WIRELESS, PRICE
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
                    SELECT CAST(ID AS VARCHAR(36)) AS ID, NAME, DPI, BUTTONS, WEIGHT, WIRELESS, PRICE
                    FROM MICE
                    WHERE ID = CAST(? AS UUID)
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
        val qry =
                queryOf(
                        """
                    INSERT INTO MICE (ID, NAME, DPI, BUTTONS, WEIGHT, WIRELESS, PRICE)
                    VALUES (CAST(? AS UUID), ?, ?, ?, ?, ?, ?)
                """.trimIndent(),
                        mice.id,
                        mice.name,
                        mice.dpi,
                        mice.buttons,
                        mice.weight,
                        mice.wireless,
                        mice.price
                )

        var result = "failed"
        sessionOf(HikariCP.dataSource()).use { conn ->
            try {
                result =
                        if (conn.run(qry.asUpdate) > 0) mice.id ?: "unknown_id"
                        else throw InternalServerErrorResponse("No se pudo insertar")
            } catch (ex: SQLException) {
                dbErrorHandler(log, ex.message)
            }
        }
        return result
    }

    fun delete(id: String): String {
        val qry =
                queryOf(
                        """
                    DELETE FROM MICE
                    WHERE ID = CHAR_TO_UUID(?)""".trimIndent(),
                        id
                )
        var result = "failed"
        sessionOf(HikariCP.dataSource()).use { conn ->
            try {
                result =
                        if (conn.run(qry.asUpdate) > 0) "success"
                        else throw InternalServerErrorResponse("No se pudo borrar")
            } catch (ex: SQLException) {
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
                    PRICE = ?
                    WHERE ID = CHAR_TO_UUID(?)""".trimIndent(),
                        mice.name,
                        mice.dpi,
                        mice.buttons,
                        mice.weight,
                        mice.wireless,
                        mice.id
                )
        var result = "failed"
        sessionOf(HikariCP.dataSource()).use { conn ->
            try {
                result =
                        if (conn.run(qry.asUpdate) > 0) "success"
                        else throw InternalServerErrorResponse("No se pudo actualizar")
            } catch (ex: SQLException) {
                dbErrorHandler(log, ex.message)
            }
        }
        return result
    }
}
