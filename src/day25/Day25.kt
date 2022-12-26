package day25

import readInput
import kotlin.math.pow

fun main() {

    fun decToSnafu(dec: Long): String {
        var cur = dec
        var snafu = ""
        while (cur > 0) {
            val mod = cur % 5

            snafu = when (mod) {
                0L -> "0"
                1L -> "1"
                2L -> "2"
                3L -> "="
                4L -> "-"
                else -> error("not mod 5")
            } + snafu

            cur /= 5
            if (mod >= 3) {
                cur++
            }
        }

        return snafu
    }

    fun snafuToDec(snafu: String): Long = snafu.reversed().map {
        when (it) {
            '2' -> 2L
            '1' -> 1L
            '0' -> 0L
            '-' -> -1L
            '=' -> -2L
            else -> error("not snafu!")
        }
    }.mapIndexed { index, num -> num * 5.0.pow(index).toLong() }.sum()

    fun part1(input: List<String>): String {
        val dec = input.sumOf { snafuToDec(it) }
        return decToSnafu(dec)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(25, true)
    check(part1(testInput) == "2=-1=0")

    val input = readInput(25)
    println(part1(input))
}
