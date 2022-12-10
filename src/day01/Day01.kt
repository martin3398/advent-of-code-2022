package day01

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        var cur = 0
        var maxVal = -1
        input.forEach {
            if (it == "") {
                if (cur > maxVal) {
                    maxVal = cur
                }

                cur = 0
            } else {
                cur += it.toInt()
            }
        }
        if (cur > maxVal) {
            maxVal = cur
        }

        return maxVal
    }

    fun part2(input: List<String>): Int {
        val calories = mutableListOf<Int>()

        var cur = 0
        input.forEach {
            if (it == "") {
                calories.add(cur)
                cur = 0
            } else {
                cur += it.toInt()
            }
        }
        calories.add(cur)

        calories.sort()

        return calories.get(calories.lastIndex) + calories.get(calories.lastIndex - 1) + calories.get(calories.lastIndex - 2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(1, true)
    check(part1(testInput) == 24000)

    val input = readInput(1)
    println(part1(input))

    check(part2(testInput) == 45000)
    println(part2(input))
}
