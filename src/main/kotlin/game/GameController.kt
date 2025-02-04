package mx.edu.uttt.mice

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.bodyValidator
import java.util.UUID
import java.util.concurrent.CompletableFuture.supplyAsync
import mx.edu.uttt.log

/*Funciones que accionan los metodos del CRUD*/
object GameController : CrudHandler {

    override fun create(ctx: Context) {
        val Game =
                ctx.bodyValidator<Game>()
                        .check({ it.title.isNotBlank() }, "El nombre no puede estar vacío")
                        .get()
                        .apply {
                            id = UUID.randomUUID().toString() // Genera un UUID válido
                            log.info("🆔 UUID generado: $id") // Imprime el UUID en los logs
                        }

        ctx.future {
            supplyAsync { GameService.insert(Game) }.thenAccept { result ->
                log.info("Resultado de la inserción: $result")
                ctx.result(result)
            }
        }
    }

    override fun delete(ctx: Context, resourceId: String) {
        ctx.future { supplyAsync { GameService.delete(resourceId) }.thenAccept(ctx::result) }
    }

    override fun getAll(ctx: Context) {
        ctx.future { supplyAsync { GameService.selectAll() }.thenAccept(ctx::json) }
    }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.future { supplyAsync { GameService.selectById(resourceId) }.thenAccept(ctx::json) }
    }

    override fun update(ctx: Context, resourceId: String) {
        ctx.bodyValidator<Game>()
                .check({ it.title.isNotBlank() }, "El nombre no puede estar vacío")
                .get()
                .apply { id = resourceId }
                .also { Game ->
                    ctx.future { supplyAsync { GameService.update(Game) }.thenAccept(ctx::result) }
                }
    }
}
