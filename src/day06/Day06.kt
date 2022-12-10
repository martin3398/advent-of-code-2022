package day06

import readInput

fun main() {

    fun part1(input: String): Int {
        for (index in 0 until input.length - 4) {
            val lastsSet = setOf<Char>(input[index], input[index + 1], input[index + 2], input[index + 3])

            if (lastsSet.size == 4) {
                return index + 4
            }
        }

        return input.length
    }

    fun part2(input: String): Int {
        for (index in 0 until input.length - 4) {
            val lastsSet = mutableSetOf<Char>()
            for (i in 0 until 14) {
                lastsSet.add(input[index + i])
            }

            if (lastsSet.size == 14) {
                return index + 14
            }
        }

        return input.length
    }

    fun preprocess(input: List<String>): String = input[0]

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(6, true)
    check(part1(preprocess(testInput)) == 10)

    val input = readInput(6)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 29)
    println(part2(preprocess(input)))
}
