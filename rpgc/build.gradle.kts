import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
    id("org.jmailen.kotlinter") version "1.20.1"
    id("io.gitlab.arturbosch.detekt") version "1.0.0-RC12"
}

group = "aph"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    junit("5.3.2")
}

fun DependencyHandlerScope.junit(version: String) {
    "org.junit.jupiter".let { group ->
        testImplementation(group, "junit-jupiter-api", version)
        testRuntime(group, "junit-jupiter-engine", version)
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    input = files("src/main/kotlin", "src/test/kotlin")
    config = files("src/test/resources/detekt.yml")
}
