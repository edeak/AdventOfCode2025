package endredeak.aoc2025.lib.utils

data class Coord(val x: Int, val y: Int) : Comparable<Coord> {
    override fun compareTo(other: Coord): Int = this.x.compareTo(other.x) + this.y.compareTo(other.y)

    fun dirs(cross: Boolean = false) =
        listOf(
            Coord(-1, -1),
            Coord(0, -1),
            Coord(1, -1),
            Coord(-1, 0),
            Coord(1, 0),
            Coord(-1, 1),
            Coord(0, 1),
            Coord(1, 1)
        ).let {
            if (cross) it else it.filter { c -> c.x == 0 || c.y == 0 }
        }

    fun neighbours(cross: Boolean = false) =
        dirs(cross).map { it + this }

    fun rotateRight() = Coord(y, -x)
    fun rotateLeft() = Coord(-y, x)

    fun diff(other: Coord) = Coord(other.x - x, other.y - y)
    operator fun plus(other: Coord): Coord = Coord(this.x + other.x, this.y + other.y)
    operator fun minus(other: Coord): Coord = Coord(this.x - other.x, this.y - other.y)
    operator fun times(i: Int): Coord = Coord(x * i, y * i)
    fun mod(other: Coord) = Coord(x.mod(other.x), y.mod(other.y))
}