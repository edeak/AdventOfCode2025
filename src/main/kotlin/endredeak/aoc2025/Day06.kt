package endredeak.aoc2025

import endredeak.aoc2025.lib.utils.product
import endredeak.aoc2025.lib.utils.transpose

fun main() {
    solve("Trash Compactor") {
        fun List<Long>.calc(op: Char) =
            when (op) {
                '*' -> this.product()
                else -> this.sum()
            }

        part1 {
            lines
                .map { l -> l.split(" ").filter { it.isNotBlank() } }
                .transpose()
                .sumOf { p ->
                    p.dropLast(1)
                        .map { it.toLong() }
                        .calc(p.last().first())
                }
        }

        part2 {
            var raw = lines
                .maxOf { it.length }
                .let { max ->
                    lines
                        .map { it.padEnd(max, ' ').toCharArray().reversed().toList() }
                        .transpose()
                }

            val groups = mutableListOf<List<List<Char>>>()
            var g = mutableListOf<List<Char>>()
            while (raw.isNotEmpty()) {
                if (raw[0].any { !it.isWhitespace() }) {
                    g.add(raw[0])
                } else {
                    groups.add(g).also { g = mutableListOf() }
                }
                raw = raw.drop(1)
            }
            groups.add(g)

            groups
                .sumOf { line ->
                    line
                        .map { it.filter { c -> c.isDigit() }.joinToString("").toLong() }
                        .calc(line.last().last())
                }
        }
    }
}