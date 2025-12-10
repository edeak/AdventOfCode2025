package endredeak.aoc2025

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

        fun nAryCounter(n: Int, k: Int): Sequence<List<Int>> = sequence {
            if (k < 1 || n < 1) return@sequence

            val max = n - 1
            val digits = IntArray(k) { 0 }

            while (true) {
                yield(digits.toList()) // Yield the current state

                var i = k - 1
                while (i >= 0 && digits[i] == max) i--

                if (i < 0) break

                digits[i]++
                for (j in i + 1 until k) digits[j] = 0
            }
        }

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

        // brute force doesn't work... need to refactor... maybe dynamic programming?
        part2(-1) {
            var count = 0L
            input.map { it.increments to it.targetJoltages }
                .forEach { (g, t) ->
                    nAryCounter(10, g.size).forEach { candidate ->
                        val curr = MutableList(t.size) { 0 }

                        candidate.forEachIndexed { i, v ->
                            g[i].forEach { b ->
                                curr[b] += v
                            }
                        }

                        if (curr == t) {
                            count += candidate.sumOf { it }
                        }
                    }
                }

            count
        }
    }
}