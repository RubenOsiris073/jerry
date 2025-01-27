package mx.edu.uttt.mice

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.bodyValidator
import java.util.UUID
import java.util.concurrent.CompletableFuture.supplyAsync
import mx.edu.uttt.Utils.properTrim

/*Funciones que accionan los metodos del CRUD*/
object MiceController: CrudHandler {

    override fun create(ctx: Context) {
        ctx.bodyValidator<Mice>()
            .check({ it.name.isNotBlank() }, "no puede ser nulo")
            .get()
            .apply {
                id = UUID.randomUUID().toString().uppercase()
                name = name.properTrim()
            }.also { mice ->
                ctx.future { supplyAsync { MiceService.insert(mice) }.thenAccept(ctx::result) }
            }
    }

    override fun delete(ctx: Context, resourceId: String) {
        ctx.future  {
            supplyAsync { MiceService.delete(resourceId) }.thenAccept(ctx::result)
        }
    }

    override fun getAll(ctx: Context) {
        ctx.future {
            supplyAsync { MiceService.selectAll() }.thenAccept(ctx::json)
        }
    }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.future {
            supplyAsync { MiceService.selectById(resourceId) }.thenAccept(ctx::json)
        }
    }

    override fun update(ctx: Context, resourceId: String) {
        ctx.bodyValidator<Mice>()
            .check({ it.name.isNotBlank() }, "no puede ser nulo")
            .get()
            .apply {
                name = name.properTrim()
            }.also { mice ->
                ctx.future {
                    supplyAsync { MiceService.update(mice) }.thenAccept(ctx::result)
                }
            }
    }
}