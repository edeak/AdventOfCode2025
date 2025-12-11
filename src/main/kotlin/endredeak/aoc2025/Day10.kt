package endredeak.aoc2025

import com.microsoft.z3.Context
import com.microsoft.z3.Expr
import com.microsoft.z3.IntNum
import com.microsoft.z3.IntSort
import com.microsoft.z3.Optimize
import com.microsoft.z3.Status
import java.util.BitSet

fun main() {
    solve("Factory") {
        data class Parsed(
            val targetVector: BitSet,
            val generatorVectors: List<BitSet>,
            val increments: List<List<Int>>,
            val targetJoltages: List<Int>
        )

        val input = lines
            .map { it.split(" ") }
            .map { line ->
                val tv = line.first().drop(1).dropLast(1)
                    .let { raw ->
                        BitSet(raw.length).apply {
                            raw.forEachIndexed { i, c -> if (c == '#') set(i) }
                        }
                    }
                val (gv, gj) = line.drop(1).dropLast(1).map { rem ->
                    rem.replace("(", "")
                        .replace(")", "").split(",")
                        .let { raw ->
                            BitSet(raw.size).apply {
                                raw.forEach { set(it.toInt()) }
                            } to raw.map { it.toInt() }
                        }
                }.let { p -> p.map { it.first } to p.map { it.second } }

                val tj = line.last().drop(1).dropLast(1).split(",").map { it.toInt() }

                Parsed(tv, gv, gj, tj)
            }

        fun allCombinations(k: Int, w: Int): List<List<Int>> {
            if (w !in 0..k) return emptyList()

            val combinations = mutableListOf<List<Int>>()
            val indices = IntArray(w) { it + 1 }
            var i: Int

            while (true) {
                combinations.add(indices.toList())

                i = w - 1
                while (i >= 0 && indices[i] == k - (w - 1 - i)) i--
                if (i < 0) break

                indices[i]++
                for (j in i + 1 until w) indices[j] = indices[j - 1] + 1
            }
            return combinations.map {
                it.map { v -> v - 1 }
            }
        }

        fun combinations(k: Int): List<List<Int>> = (1..k).map { allCombinations(k, it) }.flatten()


        part1 {
            input.sumOf { (t, g, _, _) ->
                val curr = BitSet()
                combinations(g.size)
                    .forEach { combination ->
                        combination.forEach {
                            curr.xor(g[it])
                            if (curr == t) {
                                return@sumOf combination.size.toLong()
                            }
                        }

                        curr.clear()
                    }
                0
            }
        }

        // copied from Kotlin slack aoc channel day 10 thread
        part2(-1) {
            fun <A> withZ3(f: Context.() -> A): A = Context().use { f(it) }
            fun Context.minimizeInt(target: Expr<IntSort>, constraints: Optimize.() -> Unit): Int = with(mkOptimize()) {
                val result = MkMinimize(target)
                constraints()
                check(Check() == Status.SATISFIABLE) { "constraints not satisfiable" }
                (result.value as IntNum).int
            }

            lines.map { line ->
                val parts = line.split(" ")
                val lights = parts.first().removeSurrounding("[", "]").reversed()
                    .fold(0L) { acc, ch -> acc shl 1 or if (ch == '#') 1 else 0 }
                val buttons = parts.subList(1, parts.lastIndex)
                    .map { s ->
                        s.removeSurrounding("(", ")").split(',').map { it.toInt() }
                            .fold(0L) { acc, i -> acc or (1L shl i) }
                    }
                val joltage = parts.last().removeSurrounding("{", "}").split(',').map { it.toInt() }
                Triple(lights, buttons, joltage)
            }
                .sumOf { (_, buttons, joltages) ->
                    withZ3 {
                        val buttonPresses =
                            buttons.withIndex().associate { (idx, button) -> button to mkIntConst("p$idx") }
                        val totalPresses = mkAdd(*buttonPresses.values.toTypedArray())
                        fun sumPresses(idx: Int) =
                            mkAdd(*buttonPresses.filterKeys { it ushr idx and 1L == 1L }.values.toTypedArray())

                        minimizeInt(totalPresses) {
                            // such that each joltage is the sum of presses of matching buttons
                            for ((i, joltage) in joltages.withIndex()) Add(mkEq(sumPresses(i), mkInt(joltage)))
                            // and that all presses are non-negative
                            for (it in buttonPresses) Add(mkLe(mkInt(0), it.value))
                        }
                    }
                }
        }
    }
}