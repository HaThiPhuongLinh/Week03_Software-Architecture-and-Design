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

    implementation ("gr.spinellis.ckjm:ckjm_ext:2.5")
    implementation ("au.com.dius:pact-jvm-consumer-junit:4.0.10")
    implementation ("org.apache.bcel:bcel:6.8.1")
    implementation ("guru.nidi:jdepend:2.9.5")
}

tasks.test {
    useJUnitPlatform()
}