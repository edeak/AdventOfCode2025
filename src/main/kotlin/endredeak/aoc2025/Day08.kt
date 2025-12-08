package endredeak.aoc2025

import endredeak.aoc2025.lib.utils.product
import endredeak.aoc2025.lib.utils.productOf
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.measureTimedValue

fun main() {
    solve("Playground") {
        data class C(val x: Double, val y: Double, val z: Double) {
            fun dist(other: C): Double =
                sqrt((x - other.x).pow(2) + (y - other.y).pow(2) + (z - other.z).pow(2))
        }

        val input = lines
            .map { it.split(",").let { (x, y, z) -> C(x.toDouble(), y.toDouble(), z.toDouble()) } }

        val distances = input
            .flatMap { c -> input.filter { it != c }.map { setOf(c, it) } }
            .distinct()
            .sortedBy { s -> s.take(2).let { (a, b) -> a.dist(b) } }


        fun iterate(
            cond: () -> Boolean,
            circuits: MutableSet<MutableSet<C>>,
            callback: () -> Unit
        ): Pair<Set<C>, Set<Set<C>>> =
            distances
                .toMutableList()
                .let { dists ->
                    var j: Set<C> = emptySet()
                    while (cond()) {
                        j = dists.first()
                        val matches = circuits.filter { it.intersect(j).isNotEmpty() }

                        circuits.removeAll(matches)
                        circuits.add(matches.flatMap { it }.toMutableSet())

                        dists.removeAt(0)
                        callback()
                    }

                    j to circuits
                }

        part1 {
            var c = 0
            val circuits = input.map { mutableSetOf(it) }.toMutableSet()
            iterate({ c < 1000 }, circuits) { c++ }
                .second
                .map { it.size.toLong() }.sorted()
                .reversed()
                .take(3)
                .product()
        }

        part2 {
            val circuits = input.map { mutableSetOf(it) }.toMutableSet()
            iterate({ circuits.size != 1 }, circuits) {}
                .first
                .productOf { it.x.toLong() }
        }
    }
}