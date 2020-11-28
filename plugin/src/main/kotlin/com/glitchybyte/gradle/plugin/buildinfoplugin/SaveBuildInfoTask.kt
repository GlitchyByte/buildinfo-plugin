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

package com.glitchybyte.gradle.plugin.buildinfoplugin

import org.gradle.api.DefaultTask
import org.gradle.api.InvalidUserDataException
import org.gradle.api.tasks.TaskAction
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject

/**
 * Task to autogenerate a json file with build information.
 */
open class SaveBuildInfoTask @Inject constructor(private val extension: BuildInfoPluginExtension): DefaultTask() {

    init {
        outputs.upToDateWhen { false }
    }

    /**
     * Saves the build information to all destinations.
     */
    @TaskAction
    fun save() {
        val buildInfo = BuildInfo(
                project.group as String,
                project.name,
                project.version as String,
                getDatetime(),
                getCode()
        )
        val json = buildInfo.getJson()
        saveToDestinations(json)
        println("BuildInfo: $buildInfo")
    }

    private fun saveToDestinations(json: String) {
        if (extension.destinations.isEmpty()) {
            throw InvalidUserDataException("""
                BuildInfoPlugin: No destinations given!
                Provide a configuration block. Like this:
                tasks {
                    saveBuildInfo {
                        codeBitXor = 0x11223344   // [Optional] Default is 0xff00ff00
                        filename = "my-file.json" // [Optional] Default is "build-info.json".
                        destinations = setOf(     // At least your main resource directory for the app is good.
                            "src/main/resources/com/glitchybyte/example"
                        )
                    }
                }
            """.trimIndent())
        }
        val destinationPaths = extension.destinations.map { str -> Paths.get(str, extension.filename) }
        for (path in destinationPaths) {
            val parentPath = path.parent
            if (parentPath != null) {
                Files.createDirectories(parentPath)
            }
            path.toFile().writeText(json)
        }
    }

    private fun getDatetime(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.US)
        val now = LocalDateTime.now()
        return now.format(formatter)
    }

    private fun getCode(): String {
        val generator = TimeCodeGenerator(Instant.parse("2000-01-01T00:00:00.00Z"), extension.codeBitXor)
        return generator.getCode()
    }
}
