plugins {
    id("application")
}

group = "s21.main"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    implementation ("org.slf4j:slf4j-api:2.0.16")
    implementation ("org.slf4j:slf4j-simple:2.0.16")
    implementation ("com.pi4j:pi4j-core:2.7.0")
    implementation ("com.pi4j:pi4j-plugin-raspberrypi:2.7.0")
    implementation ("com.pi4j:pi4j-plugin-gpiod:2.7.0")
}

tasks.test {
    useJUnitPlatform()
}
tasks.jar {
    manifest.attributes["Main-Class"] = "s21.main.Main"
}
java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}