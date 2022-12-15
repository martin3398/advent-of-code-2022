package day15

import readInput
import kotlin.math.abs

fun dist(fst: Pair<Int, Int>, snd: Pair<Int, Int>): Int = abs(fst.first - snd.first) + abs(fst.second - snd.second)

fun Pair<Int, Int>.add(x: Pair<Int, Int>) = Pair(this.first + x.first, this.second + x.second)
fun Pair<Int, Int>.multiplyPairwise(x: Pair<Int, Int>) = Pair(this.first * x.first, this.second * x.second)

fun main() {

    fun getBeaconsAtRow(
        input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>,
        row: Int,
    ): Set<Pair<Int, Int>> {
        val reachable = mutableSetOf<Pair<Int, Int>>()
        input.forEach {
            val dist = dist(it.first, it.second)
            var offset = 0
            var added = true
            while (added) {
                added = false
                val p1 = Pair(it.first.first + offset, row)
                val p2 = Pair(it.first.first - offset, row)
                if (dist(it.first, p1) <= dist) {
                    reachable.add(p1)
                    added = true
                }
                if (dist(it.first, p2) <= dist) {
                    reachable.add(p2)
                    added = true
                }
                offset++
            }
        }

        reachable.removeAll(input.map { it.second }.toSet())

        return reachable
    }

    fun part1(input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, row: Int): Int {
        val reachable = getBeaconsAtRow(input, row)

        return reachable.size
    }

    fun part2(input: List<Pair<Pair<Int, Int>, Pair<Int, Int>>>, maxCoordinate: Int): Long {
        input.forEach { inputEntry ->
            val (sensor, beacon) = inputEntry
            val dist = dist(sensor, beacon)

            for (xOffset in 0..dist) {
                val yOffset = dist - xOffset + 1
                val candidates = listOf(Pair(1, 1), Pair(1, -1), Pair(-1, 1), Pair(-1, -1))
                    .map { it.multiplyPairwise(Pair(xOffset, yOffset)).add(sensor) }
                    .filter { it.first in 0..maxCoordinate && it.second in 0..maxCoordinate }

                for (candidate in candidates) {
                    if (input.all { dist(it.first, candidate) > dist(it.first, it.second) }) {
                        return candidate.first.toLong() * 4000000L + candidate.second.toLong()
                    }
                }
            }
        }

        throw IllegalStateException("No Solution")
    }

    fun preprocess(input: List<String>): List<Pair<Pair<Int, Int>, Pair<Int, Int>>> = input.map {
        val split = it.split(" ")
        Pair(
            Pair(
                split[2].removePrefix("x=").removeSuffix(",").toInt(),
                split[3].removePrefix("y=").removeSuffix(":").toInt()
            ),
            Pair(
                split[8].removePrefix("x=").removeSuffix(",").toInt(),
                split[9].removePrefix("y=").toInt()
            )
        )
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput(15, true)
    check(part1(preprocess(testInput), 10) == 26)

    val input = readInput(15)
    println(part1(preprocess(input), 2000000))

    check(part2(preprocess(testInput), 20) == 56000011L)
    println(part2(preprocess(input), 4000000))
}
