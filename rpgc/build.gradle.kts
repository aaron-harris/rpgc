import java.net.URI
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
    jcenter()
    maven { url = URI.create("https://dl.bintray.com/hotkeytlt/maven") } // for betterParse
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.github.h0tk3y.betterParse:better-parse-jvm:0.4.0-alpha-3")
    junit()
}

fun DependencyHandlerScope.junit() {
    testImplementation("org.junit.platform:junit-platform-launcher:1.3.2")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.3.2")
    testImplementation("net.jqwik:jqwik:0.9.3")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

detekt {
    input = files("src/main/kotlin", "src/test/kotlin")
    config = files("src/test/resources/detekt.yml")
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("jqwik")
    }
    include("**/*Tests.class")
}
