package endredeak.aoc2025

import kotlin.math.max

fun main() {
    solve("Cafeteria") {
        val input = lines
            .filter { it.isNotBlank() }
            .partition { it.contains("-") }
            .let { (f, a) ->
                f.map { it.split("-").let { (a, b) -> LongRange(a.toLong(), b.toLong()) } } to
                        a.map { it.toLong() }
            }

        part1 {
            input.let { (fresh, available) ->
                available.count { a -> fresh.any { f -> a in f } }
            }
        }

        part2 {
            input.let { (fresh, _) ->
                var rem = fresh.sortedBy { it.first }
                var count = 0L
                var max = 0L
                while(rem.isNotEmpty()) {
                    val f = rem.first()
                    val min = max(f.first, max+1)
                    max = rem.takeWhile { it.first <= f.last }.maxOf { it.last }
                    count += (max-min)+1
                    rem = rem.filter { it.last > max }
                }
                count
            }
        }
    }
}