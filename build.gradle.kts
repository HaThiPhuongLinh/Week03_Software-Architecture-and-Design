plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation ("org.jdom:jdom2:2.0.6.1")
    implementation ("guru.nidi:jdepend:2.9.5")
}

tasks.test {
    useJUnitPlatform()
}