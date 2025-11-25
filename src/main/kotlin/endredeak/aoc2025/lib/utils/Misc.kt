@file:Suppress("unused")
package endredeak.aoc2025.lib.utils

fun String.ints() = Regex("""-?\d+""").findAll(this).map { it.value.toInt() }.toList()
fun String.longs() = Regex("""-?\d+""").findAll(this).map { it.value.toLong() }.toList()
fun String.doubles() = Regex("""-?\d+""").findAll(this).map { it.value.toDouble() }.toList()