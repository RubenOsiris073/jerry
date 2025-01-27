package mx.edu.uttt.moses

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.bodyValidator
import java.util.UUID
import java.util.concurrent.CompletableFuture.supplyAsync

object MosesController: CrudHandler {

    override fun create(ctx: Context) {
        ctx.bodyValidator<Moses>()
            .check({ it.name.isNotBlank() }, "no puede ser nulo")
            .get()
            .apply {
                id = UUID.randomUUID().toString().uppercase()
                name = name.properTrim()
            }.also { moses ->
                ctx.future { supplyAsync { MosesService.insert(moses) }.thenAccept(ctx::result) }
            }
    }

    override fun delete(ctx: Context, resourceId: String) {
        ctx.future  {
            supplyAsync { MosesService.delete(resourceId) }.thenAccept(ctx::result)
        }
    }

    override fun getAll(ctx: Context) {
        ctx.future {
            supplyAsync { DessertService.selectAll() }.thenAccept(ctx::json)
        }
    }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.future {
            supplyAsync { DessertService.selectById(resourceId) }.thenAccept(ctx::json)
        }
    }

    override fun update(ctx: Context, resourceId: String) {
        ctx.bodyValidator<Dessert>()
            .check({ it.name.isNotBlank() }, "El nombre del postre no puede ser nulo")
            .get()
            .apply {
                name = name.properTrim()
            }.also { dessert ->
                ctx.future {
                    supplyAsync { DessertService.update(dessert) }.thenAccept(ctx::result)
                }
            }
    }
}