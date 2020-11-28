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

import java.time.Instant
import java.lang.Long.toString as longToString

/**
 * Generates a code out of the current date and time.
 * The purpose of this code is to be a short and easily usable build identifier (e.g., build version, image tag, etc.).
 * The code will be unique once per second for 136 years after the zero instant. No actual words will be formed.
 */
class TimeCodeGenerator(
        private val timeKeeper: TimeKeeper, // Time keeper instance.
        private val codeBitXor: Long        // Value to xor the code with.
) {

    constructor(zeroInstant: Instant, codeBitXor: Long) : this(TimeKeeper(zeroInstant), codeBitXor)

    private val bitCount = 32 // Bits of information.
    private val base = 24 // Numeric base for encoding.
    private val charset = "0123456789bcdfghjkmpqrsx" // Characters to use for encoding.
    //                     ^^^^^^^^^^^^^^^^^^^^^^^^ <- 'base' number of characters

    /**
     * Debugging info.
     * Make sure to test this if any parameters change!
     */
    private fun printDebugLimitInfo() {
        val infoAllBits = "".padEnd(bitCount, '1')
        val infoMaxValue = infoAllBits.toLong(2)
        val years = infoMaxValue / (60 * 60 * 24 * 365)
        printDebugCode("Max", infoMaxValue)
        println("(1 per second for $years years)")
    }

    private fun printDebugCode(label: String, value: Long) {
        val bits = longToString(value, 2).padStart(bitCount, '0')
        println("$label: $bits:$value -> ${encodeValue(value)}")
    }

    /**
     * Returns time code of this second.
     */
    fun getCode(): String {
        //printDebugLimitInfo()
        val now = timeKeeper.getSeconds()
        return encodeValue(now.xor(codeBitXor))
    }

    private fun encodeValue(value: Long): String {
        val sb = StringBuilder()
        var remainder = value
        while (remainder > 0) {
            val digit = remainder % base
            remainder /= base
            sb.append(charset[digit.toInt()])
        }
        return sb.reverse().toString()
    }
}
