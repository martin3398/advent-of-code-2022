package day02

import readInput

fun main() {

    val scoreMapPart1 = mapOf<Pair<String, String>, Int>(
        Pair("A", "X") to 4,
        Pair("A", "Y") to 8,
        Pair("A", "Z") to 3,

        Pair("B", "X") to 1,
        Pair("B", "Y") to 5,
        Pair("B", "Z") to 9,

        Pair("C", "X") to 7,
        Pair("C", "Y") to 2,
        Pair("C", "Z") to 6,
    )

    val scoreMapPart2 = mapOf<Pair<String, String>, Int>(
        Pair("A", "Y") to 4,
        Pair("A", "Z") to 8,
        Pair("A", "X") to 3,

        Pair("B", "X") to 1,
        Pair("B", "Y") to 5,
        Pair("B", "Z") to 9,

        Pair("C", "Z") to 7,
        Pair("C", "X") to 2,
        Pair("C", "Y") to 6,
    )

    fun part1(input: List<Pair<String, String>>): Int = input.sumOf { scoreMapPart1[it]!! }

    fun part2(input: List<Pair<String, String>>): Int = input.sumOf { scoreMapPart2[it]!! }

    fun preprocess(input: List<String>): List<Pair<String, String>> = input.map {
        val split = it.split(" ")
        Pair(split[0], split[1])
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(2, true)
    check(part1(preprocess(testInput)) == 15)

    val input = readInput(2)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 12)
    println(part2(preprocess(input)))
}
