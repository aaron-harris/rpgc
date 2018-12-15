import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.11"
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
