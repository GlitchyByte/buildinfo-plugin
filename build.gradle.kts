// Copyright 2020 GlitchyByte
// SPDX-License-Identifier: Apache-2.0

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

tasks.named<Wrapper>("wrapper") {
    // Automatically set distribution type all when updating gradlew.
    distributionType = Wrapper.DistributionType.ALL
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
    `java-gradle-plugin`
    `maven-publish`
    id("com.google.cloud.artifactregistry.gradle-plugin") version "2.1.1"
}

repositories {
    maven(url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchy-maven/repo"))
    mavenCentral()
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_15.toString()
    }
}

dependencies {
    // Align versions of Kotlin components.
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // Test.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

group = "com.glitchybyte.gradle.plugin"
version = "1.0.0"

// Register plugin.
gradlePlugin {
    plugins {
        register("buildinfo") {
            id = "com.glitchybyte.gradle.plugin.buildinfo"
            implementationClass = "com.glitchybyte.gradle.plugin.buildinfo.BuildInfoPlugin"
        }
    }
}

// Publish to Google Cloud Platform Artifact Registry.
publishing {
    repositories {
        maven(url = uri("artifactregistry://us-west1-maven.pkg.dev/glitchy-maven/repo"))
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
