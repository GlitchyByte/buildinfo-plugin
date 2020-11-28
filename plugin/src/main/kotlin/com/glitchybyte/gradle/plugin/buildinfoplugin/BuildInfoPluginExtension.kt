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
 * Extension to configure the plugin.
 */
open class BuildInfoPluginExtension {

    /**
     * Output filename created on each destination.
     */
    var filename: String = "build-info.json"

    /**
     * Output destination directories to save the build info file.
     */
    var destinations: MutableSet<String> = HashSet()

    /**
     * 32 bit number to xor the code as it's being generated.
     * This can make the code "look" different from your other projects.
     * It has no bearing on the actual generation or availability of codes.
     */
    var codeBitXor: Long = 0xff00ff00 // 32 bits.
}
