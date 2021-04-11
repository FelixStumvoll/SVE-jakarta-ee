plugins {
    kotlin("jvm")
    kotlin("plugin.allopen")
    kotlin("plugin.noarg")
    id("io.quarkus")
}

repositories {
    mavenLocal()
    mavenCentral()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

fun quarkus(module: String) = "io.quarkus:quarkus-$module"

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation(quarkus("hibernate-orm"))
    implementation(quarkus("jdbc-h2"))
    implementation(quarkus("hibernate-validator"))
    implementation(quarkus("jdbc-mssql"))
    implementation(quarkus("resteasy-jsonb"))
    implementation(quarkus("kotlin"))
    implementation(quarkus("resteasy"))
    implementation(quarkus("resteasy-jackson"))
    implementation(quarkus("smallrye-jwt"))
    implementation(quarkus("arc"))
    implementation(quarkus("security-jpa"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation("io.quarkus:quarkus-junit5")
    testImplementation("io.rest-assured:rest-assured")
}

group = "com.urlshortener"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

allOpen {
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("javax.enterprise.context.RequestScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
    annotation("com.urlshortener.util.annotations.Open")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.ws.rs.Path")
    annotation("javax.enterprise.context.ApplicationScoped")
    annotation("javax.enterprise.context.RequestScoped")
    annotation("io.quarkus.test.junit.QuarkusTest")
    annotation("com.urlshortener.util.annotations.NoArgs")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
    kotlinOptions.javaParameters = true
}
