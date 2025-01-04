plugins {
    id("java")
}

group = "com.course.admin.catalogo.application"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":domain"))
    implementation("io.vavr:vavr:0.10.4")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("com.github.javafaker:javafaker:1.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-junit-jupiter:4.5.1")
}

tasks.test {
    useJUnitPlatform()
}