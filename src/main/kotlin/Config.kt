package mx.edu.uttt

import java.util.*
import org.slf4j.LoggerFactory

class ServerConfig(
    val jdbcProtocol: String,
    val dbHost: String,
    val dbPort: String,
    val dbName: String,
    val dbUser: String,
    val dbPwd: String,
    val dbEncoding: String
) {
    val dbURL
        get() = "$jdbcProtocol://$dbHost:$dbPort/$dbName?encoding=$dbEncoding"
}

object Config {

    private val log = LoggerFactory.getLogger(this::class.java)
    val SVR_CONF: ServerConfig

    init {
        // Cargar el archivo desde el classpath
        val prop = Properties().apply {
            val inputStream = Config::class.java.classLoader.getResourceAsStream("config/server.properties")
                ?: throw IllegalStateException("Archivo config/server.properties no encontrado en el classpath")
            load(inputStream)
        }

        // Inicializar la configuración del servidor
        SVR_CONF = ServerConfig(
            jdbcProtocol = prop.getProperty("database.jdbc.protocol"),
            dbHost = prop.getProperty("database.host"),
            dbPort = prop.getProperty("database.port"),
            dbName = prop.getProperty("database.name"),
            dbUser = prop.getProperty("database.user"),
            dbPwd = prop.getProperty("database.password"),
            dbEncoding = prop.getProperty("database.encoding")
        )

        // Loggear la configuración cargada
        log.info("Database url: {}", SVR_CONF.dbURL)
        log.info("Database user: {}", SVR_CONF.dbUser)
    }

    fun showBanner() {
        println(javaClass.classLoader.getResource("banner.txt")?.readText())
    }
}