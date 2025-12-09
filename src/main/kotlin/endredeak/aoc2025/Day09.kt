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

        part1 {
            pairs.maxOf { (p1, p2) ->
                (p2.y - p1.y + 1L) * (p2.x - p1.x + 1L)
            }
        }

        part2 {
            pairs.filter { (p1, p2) ->
                edges.none { (l1, l2) ->
                    p1.x < l2.x && p2.x > l1.x && p1.y < l2.y && p2.y > l1.y
                }
            }.maxOf { (p1, p2) ->
                (p2.y - p1.y + 1L) * (p2.x - p1.x + 1L)
            }
        }
    }
}