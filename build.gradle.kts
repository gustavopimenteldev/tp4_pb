plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    api(libs.org.springframework.boot.spring.boot.starter.web)
    api(libs.org.springframework.boot.spring.boot.starter.thymeleaf)
    api(libs.org.springframework.boot.spring.boot.starter.validation)
    testImplementation(libs.org.springframework.boot.spring.boot.starter.test)
    testImplementation(libs.net.jqwik.jqwik)
    testImplementation(libs.org.seleniumhq.selenium.selenium.java)
    testImplementation(libs.io.github.bonigarcia.webdrivermanager)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.2")
}

group = "com.sistema"
version = "1.0.0"
description = "crud-system"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    exclude("**/*SeleniumTest.class")
    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
    }
}

tasks.register<Test>("seleniumTest") {
    description = "Runs Selenium UI tests (browser end-to-end)"
    group = "verification"

    useJUnitPlatform()

    include("**/*SeleniumTest.class")

    systemProperty("headless", System.getProperty("headless") ?: "true")
}
