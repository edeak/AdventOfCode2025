package endredeak.aoc2025.lib.utils

typealias Grid<T> = MutableMap<Coord, T & Any>

fun <T> List<String>.toGrid(mod: (Char) -> T?): Grid<T> =
    this
        .flatMapIndexed { y, l ->
            l.mapIndexedNotNull { x, c ->
                mod(c)?.let { Coord(x, y) to it}
            }
        }
        .associate { it }
        .toMutableMap()

fun Grid<*>.print() = entries.groupBy { it.key.y }.toSortedMap()
    .forEach { (_, row) ->
        row.sortedBy { it.key.x }.forEach { print(it.value) }
        println()
    }

fun <T> Grid<T>.single(predicate: (T) -> Boolean): Coord = filterValues(predicate).keys.single()