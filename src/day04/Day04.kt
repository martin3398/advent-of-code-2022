package day04

import readInput

typealias Day04InputType1 = List<Pair<Pair<Int, Int>, Pair<Int, Int>>>

fun main() {

    fun fullyOverlaps(fst: Pair<Int, Int>, snd: Pair<Int, Int>): Boolean =
        (fst.first <= snd.first && fst.second >= snd.second) || (fst.first >= snd.first && fst.second <= snd.second)

    fun overlaps(fst: Pair<Int, Int>, snd: Pair<Int, Int>): Boolean =
        (fst.first >= snd.first && fst.first <= snd.second) ||
                (fst.second >= snd.first && fst.second <= snd.second) ||
                fullyOverlaps(fst, snd)

    fun part1(input: Day04InputType1): Int = input.map {
        if (fullyOverlaps(it.first, it.second)) 1 else 0
    }.sum()

    fun part2(input: Day04InputType1): Int = input.map {
        if (overlaps(it.first, it.second)) 1 else 0
    }.sum()

    fun preprocess(input: List<String>): Day04InputType1 = input.map {
        val split = it.split(",")
        val split0 = split[0].split("-")
        val split1 = split[1].split("-")
        Pair(Pair(split0[0].toInt(), split0[1].toInt()), Pair(split1[0].toInt(), split1[1].toInt()))
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(4, true)
    check(part1(preprocess(testInput)) == 2)

    val input = readInput(4)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 4)
    println(part2(preprocess(input)))
}
