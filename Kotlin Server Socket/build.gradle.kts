import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.10"
    application
}

group = "me.sal7_one"
version = "4.0"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
            dependencies {
                implementation(kotlin("test")) // This brings all the platform dependencies automatically
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.10")
        }
    }
}
dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.10")
}

tasks.jar {
    manifest.attributes["Main-Class"] = "com.sal7one.socketserver.MainKt"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree) // OR .map { zipTree(it) }
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}