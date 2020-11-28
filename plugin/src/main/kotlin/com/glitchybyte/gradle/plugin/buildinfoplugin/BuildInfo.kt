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

/**
 * Build information.
 */
class BuildInfo(
        val group: String,    // Project group.
        val name: String,     // Project name.
        val version: String,  // Project version.
        val datetime: String, // Build datetime stamp.
        val code: String      // Build version code.
) {

    fun getJson(): String {
        // Make a map.
        val map = mapOf(
                "group" to group,
                "name" to name,
                "version" to version,
                "datetime" to datetime,
                "code" to code
        )
        // Build json.
        val sb = StringBuilder()
        sb.append("{\n")
        val count = map.size
        var index = 0
        for (pair in map) {
            sb.append("  \"${pair.key}\": \"${pair.value}\"")
            ++index
            if (index < count) {
                sb.append(",\n")
            } else {
                sb.append('\n')
            }
        }
        sb.append("}\n")
        return sb.toString()
    }

    override fun toString(): String {
        return "$group:$name:$version ($code) $datetime"
    }
}
