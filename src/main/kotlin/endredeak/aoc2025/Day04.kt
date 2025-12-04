package endredeak.aoc2025

import endredeak.aoc2025.lib.utils.Coord
import endredeak.aoc2025.lib.utils.toGrid

fun main() {
    solve("Printing Department") {
        val input = lines.toGrid { it }

        fun Map.Entry<Coord, Char>.removable() =
            this.value == '@' && this.key.neighbours(true).count { c -> input[c] == '@'} < 4

        part1 {
            input.count { it.removable() }
        }

        part2 {
            var sum = 0
            while(true) {
                input
                    .filter { it.removable() }
                    .takeIf { it.isNotEmpty() }
                    ?.forEach { (c, _) -> input[c] = '.'; sum++ }
                    ?: break
            }
            sum
        }
    }
}