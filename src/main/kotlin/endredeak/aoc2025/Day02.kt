package endredeak.aoc2025

fun main() {
    solve("Gift Shop") {
        val input = lines[0]
            .split(",")
            .map { it.split("-") }
            .map { (f, s) -> f.toLong()..s.toLong() }

        fun calc(check: Long.() -> Boolean) = input.sumOf { r -> r.filter { check(it) }.sum() }

        part1(17077011375) {
            calc { "$this".let { s -> (s.length / 2).let { s.take(it) == s.drop(it) } } }
        }

        part2(36037497037) {
            calc { "$this".matches(Regex("(.+)\\1+")) }
        }
    }
}