package endredeak.aoc2025

fun main() {
    solve("Lobby") {
        val input = lines

        fun maxJoltage(bank: String, n: Int): String =
            if (bank.isNotEmpty() && n != 0)
                bank
                    .dropLast(n - 1)
                    .withIndex()
                    .maxBy { it.value }
                    .let { max -> max.value + maxJoltage(bank.drop(max.index + 1), n - 1) }
            else ""

        part1(17263) { input.sumOf { maxJoltage(it, 2).toLong() } }

        part2(3121910778619) { input.sumOf { maxJoltage(it, 12).toLong() } }
    }
}