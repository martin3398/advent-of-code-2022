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
                3L -> "="
                4L -> "-"
                else -> mod.toString()
            } + snafu

            cur = cur / 5 + mod / 3
        }

        return snafu
    }

    fun snafuToDec(snafu: String): Long = snafu.reversed().map {
        when (it) {
            '-' -> -1L
            '=' -> -2L
            else -> it.digitToInt().toLong()
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
