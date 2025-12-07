package endredeak.aoc2025

fun main() {
    solve("Laboratories") {
        val input = lines
            .map { it.toCharArray().toMutableList() }
            .toMutableList()

        part1 {
            var count = 0
            input[1][input[0].indexOf('S')] = '|'
            input.indices.drop(2).forEach { y ->
                input[y].indices.forEach { x ->
                    if (input[y-1][x] == '|') {
                        when(input[y][x]) {
                            '.' -> input[y][x] = '|'
                            '^' -> {
                                input[y][x - 1] = '|'
                                input[y][x + 1] = '|'
                                count++
                            }
                        }
                    }
                }
            }
            count
        }

        part2 {
            var map = mapOf(input.first().indexOf('S') to 1L)
            input.indices.drop(1).forEach { y ->
                map = buildMap {
                    map.forEach { (x, v) ->
                        if (input[y][x] == '^') {
                            put(x - 1, v + (this[x - 1] ?: 0))
                            put(x + 1, v)
                        } else put(x, v + (this[x] ?: 0))
                    }
                }
            }
            map.values.sum()
        }
    }
}