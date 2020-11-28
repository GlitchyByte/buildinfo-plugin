/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package com.glitchybyte.gradle.plugin.buildinfoplugin

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class GlitchyBuildPluginTest {
    @Test
    fun pluginRegistersTask() {
        // Create a test project and apply the plugin
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.glitchybyte.gradle.plugin.buildinfoplugin")

        // Verify the result
        assertNotNull(project.tasks.findByName("saveBuildInfo"))
    }
}