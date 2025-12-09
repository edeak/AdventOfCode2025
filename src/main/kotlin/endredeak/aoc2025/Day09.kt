package endredeak.aoc2025

import endredeak.aoc2025.lib.utils.Coord
import kotlin.math.max
import kotlin.math.min

fun main() {
    solve("Movie Theater") {
        fun Pair<Coord, Coord>.sort(): Pair<Coord, Coord> = Pair(
            Coord(min(first.x, second.x), min(first.y, second.y)),
            Coord(max(first.x, second.x), max(first.y, second.y))
        )

        val coords: List<Coord> = lines
            .map { it.split(",").let { (x, y) -> Coord(x.toInt(), y.toInt()) } }

        val pairs = coords
            .flatMapIndexed { y, c -> coords.drop(y + 1).map { c to it } }
            .toSet()
            .map { it.sort() }

        val edges = coords
            .plus(coords.first())
            .zipWithNext()
            .map { it.sort() }

        fun Pair<Coord, Coord>.squared() = (second.y - first.y + 1L) * (second.x - first.x + 1L)

        part1 {
            pairs.maxOf { it.squared() }
        }

        part2 {
            pairs.filter { (r1, r2) ->
                edges.none { (e1, e2) ->
                    r1.x < e2.x && r2.x > e1.x && r1.y < e2.y && r2.y > e1.y
                }
            }.maxOf { it.squared() }
        }
    }
}