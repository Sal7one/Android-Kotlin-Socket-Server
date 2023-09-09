import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.22"
    application
}

group = "me.sal7_one"
version = "5.0"

repositories {
    mavenCentral()
}

kotlin {
    sourceSets {
            dependencies {
                implementation(kotlin("test")) // This brings all the platform dependencies automatically
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
        }
    }
}
dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
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
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("MainKt")
}