package day20

import readInput

fun main() {

    fun simulate(input: List<Pair<Int, Long>>, factor: Long = 1L, iterations: Int = 1): Long {
        val inputFactorized = input.map { it.first to it.second * factor }
        val list = inputFactorized.toMutableList()
        repeat(iterations) {
            inputFactorized.forEach {
                val index = list.indexOf(it)
                list.removeAt(index)
                list.add((index + it.second).mod(list.size), it)
            }
        }

        val zeroPos = list.indexOfFirst { it.second == 0L }
        return listOf(1000, 2000, 3000).sumOf { list[(zeroPos + it) % list.size].second }
    }

    fun part1(input: List<Pair<Int, Long>>) = simulate(input).toInt()

    fun part2(input: List<Pair<Int, Long>>) = simulate(input, 811589153L, 10)

    fun preprocess(input: List<String>): List<Pair<Int, Long>> = input.mapIndexed { index, s -> index to s.toLong() }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(20, true)
    check(part1(preprocess(testInput)) == 3)

    val input = readInput(20)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 1623178306L)
    println(part2(preprocess(input)))
}
