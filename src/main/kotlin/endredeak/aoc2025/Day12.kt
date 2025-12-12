package endredeak.aoc2025

fun main() {
    solve("Christmas Tree Farm") {
        part1 {
            text.replace("x", " ").replace(":", "").split("\n\n")
                .partition { it.contains("#") }
                .let { (s, r) ->
                    val shapes = s.mapIndexed { i, s -> i to s.count { ch -> ch == '#' } }.associate { it }

                    r[0].split("\n")
                        .map { l -> l.split(" ").map { it.toInt() } }
                        .count {
                            it[0] * it[1] >= it.drop(2).mapIndexed { i, c -> shapes[i]!! * c }.sum()
                        }
                }
        }

        part2 {

        }
    }
}