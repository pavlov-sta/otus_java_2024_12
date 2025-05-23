rootProject.name = "otusJava"
include("hw01-gradle")
include("L03-generics")
include("L06-annotations")

include("L08-gc:homework")
include("L10-Loader")
include("L12-solid")

include("L15-structuralPatterns:homework")

include("L16-io:homework")

include("L18-jdbc:demo")
include("L18-jdbc:homework")

include("L21-jpql:class-demo")
include("L21-jpql:homework-template")

include("L22-cache")

include("L24-webServer")

include("L25-di:class-demo")
include("L25-di:homework-template")

include ("L28-springDataJdbc")
pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}