// Copyright 2020 GlitchyByte LLC
// SPDX-License-Identifier: Apache-2.0

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
    `java-gradle-plugin`
    `maven-publish`
    id("com.google.cloud.artifactregistry.gradle-plugin") version "2.1.1"
}

repositories {
    maven {
        url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchy-maven/repo")
    }
    mavenCentral()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_15.toString()
    }
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

group = "com.glitchybyte.gradle.plugin"
version = "1.0.3"

tasks.withType<Jar>() {
    archiveBaseName.set("buildinfo-plugin")
}

gradlePlugin {
    plugins {
        create("buildinfoplugin") {
            id = "com.glitchybyte.gradle.plugin.buildinfoplugin"
            implementationClass = "com.glitchybyte.gradle.plugin.buildinfoplugin.BuildInfoPlugin"
        }
    }
}

// Add a source set for functional test suite.
val functionalTestSourceSet = sourceSets.create("functionalTest") {
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations["functionalTestImplementation"].extendsFrom(configurations["testImplementation"])

// Add a task to run functional tests.
val functionalTest by tasks.registering(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

tasks.named("check") {
    // Run functional tests as part of "check".
    dependsOn(functionalTest)
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories {
        maven {
            url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchy-maven/repo")
        }
    }
    publications {
        register<MavenPublication>("library") {
            groupId = project.group as String
            artifactId = "buildinfo-plugin"//project.name.toLowerCase(Locale.US)
            version = project.version as String
            from(components["kotlin"])
            artifact(tasks["sourcesJar"])
            artifact(tasks["javadocJar"])
            pom {
                name.set("BuildInfoPlugin")
                description.set("Generates build information file.")
                url.set("https://github.com/glitchybyte/buildinfo-plugin")
                licenses {
                    license {
                        name.set("Apache License 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
            }
        }
    }
}
