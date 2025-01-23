plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "mx.edu.uttt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    /* Webserver, Data Logger */
    implementation("io.javalin:javalin:6.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.16")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")
    /* Vue 3, Vuetify 3 & Material Design */
    implementation("org.webjars.npm:vue:3.5.4")
    implementation("org.webjars.npm:vuetify:3.7.0")
    implementation("org.webjars.npm:mdi__font:7.4.47")
    implementation("org.webjars:font-awesome:6.5.2")
    implementation("org.webjars.npm:roboto-fontface:0.10.0")

    implementation("org.webjars.npm:sweetalert2:11.14.3")

    /* Database Pool Connexion, Firebird JDBC Driver & ORM */
    implementation("com.zaxxer:HikariCP:6.0.0")
    implementation("org.firebirdsql.jdbc:jaybird:5.0.5.java11")
    implementation("com.github.seratch:kotliquery:1.9.0")
    implementation("org.mindrot:jbcrypt:0.4")
}

application {
    mainClass.set("mx.edu.uttt.MainKt")
}
kotlin {
    jvmToolchain(21)
}