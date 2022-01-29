plugins {
    id ("com.github.ben-manes.versions") version "0.39.0"
    id ("com.adarshr.test-logger") version "3.1.0"
    java
    checkstyle
    jacoco
    application
}

group = "hexlet.code"
version = "1.0.0-SNAPSHOT"


repositories {
    mavenCentral()
}


dependencies {
    listOf(
        "org.jetbrains:annotations:22.0.0",
        "org.slf4j:slf4j-simple:1.7.35",
        "io.javalin:javalin:4.3.0",
        "com.fasterxml.jackson.core:jackson-databind:2.12.4"
    ).forEach(::implementation)

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks {
    withType<JavaCompile> {
        options.release.set(16)
    }

    withType<Checkstyle>().configureEach {

        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }

    withType<Test> {
        useJUnitPlatform()
        finalizedBy(jacocoTestReport)
    }

    jacocoTestReport {
        dependsOn(test)
        reports {
            xml.required.set(true)
            csv.required.set(false)
            html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
        }
    }

}


val copyToLib by tasks.registering(Copy::class) {
    doLast {
        from(configurations.implementation)
        into("$buildDir/libs")
    }
}

val stage by tasks.registering {
    dependsOn("build")
    dependsOn(copyToLib)
}


val jar by tasks.getting(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
        attributes["Main-Class"] = "hexlet.code.App"
    }

    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}


application {
    mainClass.set("hexlet.code.App")
}

