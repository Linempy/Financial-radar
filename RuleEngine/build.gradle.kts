plugins {
    id("org.springframework.boot")
    id("com.github.davidmc24.gradle.plugin.avro")
    id("io.spring.dependency-management")
    id("java")
}

dependencies {
    /**
     * Spring boot starters
     */
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    implementation("org.springframework.retry:spring-retry")

    /**
     * Database
     */
    implementation("org.liquibase:liquibase-core")
    implementation("redis.clients:jedis:4.3.2")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:6.2.5.Final")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    /**
     * Utils & Logging
     */
    implementation("com.fasterxml.jackson.core:jackson-databind:2.14.2")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.6")
    implementation("org.projectlombok:lombok:1.18.26")
    annotationProcessor("org.projectlombok:lombok:1.18.26")
    implementation("org.mapstruct:mapstruct:1.5.3.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.3.Final")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.0.2")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.13.0")

    /**
     * Test containers
     */
    implementation(platform("org.testcontainers:testcontainers-bom:1.17.6"))
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("com.redis.testcontainers:testcontainers-redis-junit-jupiter:1.4.6")
    testImplementation("org.testcontainers:minio:1.19.5")

    /**
     * Tests
     */
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    /**
     * Swagger
     */
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    /**
     * Message Broker
     */
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.apache.avro:avro:1.11.1")
    implementation("io.confluent:kafka-avro-serializer:7.4.0")
    implementation("io.confluent:kafka-schema-registry-client:7.4.0")
    testImplementation("org.testcontainers:kafka:1.19.3")
    testImplementation("org.testcontainers:junit-jupiter:1.19.3")

    /**
     * Micrometer
     */
    implementation ("io.micrometer:micrometer-registry-prometheus")

    implementation(project(":TransactionalService"))

    /**
     * Логи
     */
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("com.github.loki4j:loki-logback-appender:1.2.0")
}

tasks.test {
    useJUnitPlatform()
}

avro {
    isCreateSetters = false
    fieldVisibility = "PRIVATE"
    outputCharacterEncoding = "UTF-8"
}

tasks.named<com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask>("generateAvroJava") {
    setSource(file("src/main/resources/avro"))
    setOutputDir(file("$buildDir/generated-sources/avro"))
}
