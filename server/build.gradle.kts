import com.google.protobuf.gradle.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm")
    id("com.google.protobuf")
    id("org.jetbrains.kotlin.kapt")
}

dependencies {
    protobuf(project(":protos"))

    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
    implementation("io.grpc:grpc-kotlin-stub:0.1.1")

    implementation("com.google.protobuf:protobuf-java:3.6.1")
    implementation("io.grpc:grpc-stub:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")
    implementation("io.grpc:grpc-protobuf:${rootProject.ext["grpcVersion"]}")
    api("io.grpc:grpc-kotlin-stub:${rootProject.ext["grpcKotlinVersion"]}")

    runtimeOnly("io.grpc:grpc-netty:${rootProject.ext["grpcVersion"]}")

    implementation("org.litote.kmongo:kmongo:4.2.3")
    implementation("org.litote.kmongo:kmongo-async:4.2.3")
    implementation("org.litote.kmongo:kmongo-coroutine:4.2.3")
    annotationProcessor("org.litote.kmongo:kmongo-annotation-processor:4.2.3")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("de.bwaldvogel:mongo-java-server:1.37.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}

protobuf {

    protoc {
        // The artifact spec for the Protobuf Compiler
        artifact = "com.google.protobuf:protoc:${rootProject.ext["protobufVersion"]}"
    }
    plugins {
        // Optional: an artifact spec for a protoc plugin, with "grpc" as
        // the identifier, which can be referred to in the "plugins"
        // container of the "generateProtoTasks" closure.
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${rootProject.ext["grpcVersion"]}"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:${rootProject.ext["grpcKotlinVersion"]}:jdk7@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
        }
    }
}

tasks.register<JavaExec>("CarCatalogueServer") {
    dependsOn("classes")
    classpath = sourceSets["main"].runtimeClasspath
    main = "com.carcatalogue.server.CarCatalogueServerKt"
}

val carCatalogueServerStartScripts = tasks.register<CreateStartScripts>("carCatalogueServerStartScripts") {
    mainClassName = "com.carcatalogue.server.CarCatalogueServerKt"
    applicationName = "car-catalogue-server"
    outputDir = tasks.named<CreateStartScripts>("startScripts").get().outputDir
    classpath = tasks.named<CreateStartScripts>("startScripts").get().classpath
}

tasks.named("startScripts") {
    dependsOn(carCatalogueServerStartScripts)
}

java {
    sourceSets.getByName("main").resources.srcDir("src/main/proto")
    sourceSets.getByName("main").java.srcDir("build/generated/source/proto/main/java")
    sourceSets.getByName("main").java.srcDir("build/generated/source/proto/main/grpc")
    sourceSets.getByName("main").java.srcDir("build/generated/source/proto/main/grpckt")
}