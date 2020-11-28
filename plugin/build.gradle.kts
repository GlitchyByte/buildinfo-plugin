/*
 * Copyright 2020 Luis Mejia
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.20"
    `java-gradle-plugin`
    `maven-publish`
}

repositories {
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.4.20")
    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "15"
    }
}

group = "com.glitchybyte.gradle.plugin"
version = "1.0.0"

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

//publishing {
//    publications {
//        create<MavenPublication>("library") {
//            groupId = project.group as String
//            artifactId = "glitchybuildplugin"//project.name.toLowerCase(Locale.US)
//            version = project.version as String
//            from(components["kotlin"])
//            pom {
//                name.set("GlitchyBuild")
//                description.set("Build utilities.")
//                url.set("https://github.com/wyvx/glitchybuild")
//                licenses {
//                    license {
//                        name.set("Apache License 2.0")
//                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
//                    }
//                }
//            }
//        }
//    }
//}
