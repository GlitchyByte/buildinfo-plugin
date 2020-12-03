// Copyright 2020 GlitchyByte LLC
// SPDX-License-Identifier: Apache-2.0

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
    var destinations: Set<String> = HashSet()

    /**
     * 32 bit number to xor the code as it's being generated.
     * This can make the code "look" different from your other projects.
     * It has no bearing on the actual generation or availability of codes.
     */
    var codeBitXor: Long = 0xff00ff00 // 32 bits.
}
