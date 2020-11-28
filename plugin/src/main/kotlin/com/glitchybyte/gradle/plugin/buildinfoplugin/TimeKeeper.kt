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

import java.time.Duration
import java.time.Instant

/**
 * Basic time keeping class to have a good starting time for future builds.
 */
class TimeKeeper(
        private val zeroInstant: Instant // Zero time marker.
) {

    /**
     * Returns seconds since zero instant.
     */
    fun getSeconds(): Long {
        val now = Instant.now()
        val duration = Duration.between(zeroInstant, now)
        return duration.toSeconds()
    }
}
