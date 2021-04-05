import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Versions.springVersion
    kotlin("jvm") version Versions.kotlinVersion
    kotlin("plugin.spring") version Versions.kotlinVersion
    kotlin("plugin.jpa") version Versions.kotlinVersion
    kotlin("kapt") version Versions.kotlinVersion
}

group = "com.urlshortener"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}


dependencies {
    //spring
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${Versions.springVersion}"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    kapt("org.springframework.boot:spring-boot-configuration-processor:${Versions.springVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    //kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    //db
    implementation("com.h2database:h2")
    runtimeOnly("com.microsoft.sqlserver:mssql-jdbc")
    implementation("org.connectbot:jbcrypt:1.0.2")

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
        useIR = true
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
