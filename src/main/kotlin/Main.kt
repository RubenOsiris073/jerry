package mx.edu.uttt

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.staticfiles.Location
import io.javalin.vue.VueComponent
import java.util.logging.Logger
import java.util.logging.Logger.getLogger
import kotliquery.HikariCP
import mx.edu.uttt.Config.SVR_CONF
import mx.edu.uttt.Config.showBanner
import mx.edu.uttt.mice.MiceController

val log: Logger = getLogger("Main")

fun main() {
    //    HikariCP.default(
    //        "jdbc:firebirdsql://localhost:3050/mice?encoding=UTF8",
    //        "sysdba",
    //        "MyPass123$"
    //    )
    HikariCP.default(SVR_CONF.dbURL, SVR_CONF.dbUser, SVR_CONF.dbPwd)
    val app =
            Javalin.create { config ->
                config.staticFiles.apply {
                    enableWebjars()
                    add("public", Location.CLASSPATH)
                }
                config.vue.apply {
                    vueInstanceNameInJs = "app"
                    rootDirectory("/vue", Location.CLASSPATH)
                }
                config.router.mount {}.apiBuilder { // Entry Points
                    get("/", VueComponent("mice-page"))
                    path("api") { // Restfull/Endpoints
                        println("✅ Registrando CRUD en /api/mice")
                        crud("mice/{id}", MiceController)
                    }
                }
                

                config.requestLogger.http { ctx, execTimeMs ->
                    if (ctx.req().getAttribute("handled-as-static-file") == null &&
                                    ctx.matchedPath().isNotBlank()
                    ) {
                        log.info(
                                "IP:${ctx.ip()} ${ctx.method()} ${ctx.matchedPath()} completed in ${execTimeMs} ms"
                        )
                    }
                }
                /* Show Custom Banner SHERK */
                config.showJavalinBanner = false
                showBanner()
            }
            app.events { event ->
                event.serverStarted {
                    println("🔹 Rutas registradas en Javalin:")
                    app.jettyServer().server().handler?.let { handler ->
                        println(handler)
                    }
                }
            }
    app.start()
}
