package endredeak.aoc2025

fun main() {
    solve("Reactor") {
        val input = lines.associate { it.split(": ").let { (k, v) -> k to v.split(" ") } }

        fun calc(src: String, dst: String) : Long =
            with(mutableMapOf<String, Long>()) {
                return DeepRecursiveFunction { key ->
                    if (key == dst) 1L else getOrPut(key) { input[key]?.sumOf { callRecursive(it) } ?: 0 }
                }(src)
            }

        part1 { calc("you", "out") }

        part2 {
            calc("svr", "fft") * calc("fft", "dac") * calc("dac", "out") +
                    calc("svr", "dac") * calc("dac", "fft") * calc("fft", "out")
        }
    }
}