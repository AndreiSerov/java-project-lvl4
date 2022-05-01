plugins {
    id("com.github.ben-manes.versions") version "0.39.0"
    id("com.adarshr.test-logger") version "3.1.0"
    id("io.ebean") version "12.14.1"
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
    runtimeOnly("com.h2database:h2:2.1.210")

    annotationProcessor("io.ebean:querybean-generator:12.15.0")

    listOf(
        "org.jetbrains:annotations:22.0.0",
        "org.slf4j:slf4j-simple:1.7.35",
        "io.javalin:javalin:4.3.0",
        "com.fasterxml.jackson.core:jackson-databind:2.12.1",

        "org.postgresql:postgresql:42.3.1",
        "io.ebean:ebean:12.14.1",
        "io.ebean:ebean-annotation:7.4",
        "io.ebean:ebean-migration:12.13.0",
        "io.ebean:ebean-ddl-generator:12.14.1",
        "io.ebean:ebean-querybean:12.14.1",
        "org.glassfish.jaxb:jaxb-runtime:2.3.5",

        "org.thymeleaf:thymeleaf:3.0.12.RELEASE",
        "nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.0.0",
        "org.thymeleaf.extras:thymeleaf-extras-java8time:3.0.4.RELEASE",
        "org.webjars:bootstrap:5.1.1",

        "com.konghq:unirest-java:3.11.09",
        "org.jsoup:jsoup:1.14.3",
    ).forEach(::implementation)

    listOf(
        "org.junit.jupiter:junit-jupiter:5.8.2",
        "io.ebean:ebean-test:12.12.1",
        "org.assertj:assertj-core:3.22.0",
    ).forEach(::testImplementation)
}

tasks {

    create("stage").dependsOn("build", "installDist")

    installDist {
        mustRunAfter(clean)
    }

    withType<JavaCompile> {
        options.release.set(17)
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

val generateMigrations by tasks.registering(JavaExec::class) {
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("hexlet.code.config.MigrationGenerator")
}

//val copyToLib by tasks.registering(Copy::class) {
//    doLast {
//        from(configurations.implementation)
//        into("$buildDir/libs")
//    }
//}




//val jar by tasks.getting(Jar::class) {
//    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
//
//    manifest {
//        attributes["Main-Class"] = "hexlet.code.App"
//    }
//
//    configurations["compileClasspath"].forEach { file: File ->
//        from(zipTree(file.absoluteFile))
//    }
//}


application {
    mainClass.set("hexlet.code.App")
}

