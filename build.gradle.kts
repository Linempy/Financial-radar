// build.gradle.kts
plugins {
    id("org.springframework.boot") version "3.2.0" apply false
    id("io.spring.dependency-management") version "1.1.4" apply false
    id("com.github.davidmc24.gradle.plugin.avro") version "1.9.1" apply false
}

allprojects {
    group = "com.financeRadar.manticore"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
        maven(url = "https://packages.confluent.io/maven/")
        gradlePluginPortal()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")

    the<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension>().apply {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}