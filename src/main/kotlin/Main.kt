package mx.edu.uttt
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.staticfiles.Location
import io.javalin.vue.VueComponent
import kotliquery.HikariCP
import mx.edu.uttt.mice.MiceController
import org.slf4j.LoggerFactory
import java.util.logging.Logger
import mx.edu.uttt.Config.SVR_CONF
import mx.edu.uttt.Config.showBanner

val log: Logger = LoggerFactory.getLogger("Main")

fun main() {
    HikariCP.default(SVR_CONF.dbURL, SVR_CONF.dbUser, SVR_CONF.dbPwd)
    val app = Javalin.create { config ->
        config.staticFiles.apply {
            enableWebjars()
            add("public", Location.CLASSPATH)
        }
        config.vue.apply {
            vueInstanceNameInJs = "app"
            rootDirectory("/vue", Location.CLASSPATH)
        }
        config.router.mount {
        }.apiBuilder { //Entry Points
            get("/", VueComponent("mice-page"))
            path("api") { // Restfull/Endpoints
                crud("mice/{id}", MiceController)
            }
        }

        config.requestLogger.http { ctx, execTimeMs ->
            if (ctx.req().getAttribute("handled-as-static-file") == null && ctx.matchedPath().isNotBlank()) {
                log.info("IP:{} {} {} completed in {} ms", ctx.ip(), ctx.method(), ctx.matchedPath(), execTimeMs)
            }
        }
        /* Show Custom Banner SHERK */
        config.showJavalinBanner = false
        showBanner()
    }.start()
}