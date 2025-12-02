package endredeak.aoc2025

fun main() {
    solve("Secret Entrance") {
        val input = lines.map { it.first() to it.drop(1).toInt() }

        part1 {
            var dial = 50
            var count = 0
            input
                .forEach { (dir, amount) ->
                    val d = if (dir == 'R') 1 else -1
                    dial += d * amount
                    dial = dial.mod(100)
                    if (dial == 0) count++
                }
            count
        }

        part2 {
            var dial = 50
            var count = 0
            input
                .forEach { (dir, amount) ->
                    val d = if (dir == 'R') 1 else -1
                    repeat(amount) {
                        dial += d
                        dial = dial.mod(100)
                        if (dial == 0) count++
                    }
                }
            count
        }
    }
}