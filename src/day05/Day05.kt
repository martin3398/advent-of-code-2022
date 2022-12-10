package day05

import readInput

typealias Day05InputType1 = Pair<Map<Int, List<Char>>, List<Triple<Int, Int, Int>>>

fun main() {

    fun part1(input: Day05InputType1): String {
        val crateStatus = input.first.mapValues { it.value.toMutableList() }

        input.second.forEach {
            for (i in 0 until it.first) {
                val popped = crateStatus[it.second]?.removeLast()!!
                crateStatus[it.third]?.add(popped)
            }
        }

        return String(crateStatus.map { it.value.last() }.toCharArray())
    }

    fun part2(input: Day05InputType1): String {
        val crateStatus = input.first.mapValues { it.value.toMutableList() }

        input.second.forEach {
            val toAdd = mutableListOf<Char>()
            for (i in 0 until it.first) {
                toAdd.add(crateStatus[it.second]?.removeLast()!!)
            }
            crateStatus[it.third]?.addAll(toAdd.reversed())
        }

        return String(crateStatus.map { it.value.last() }.toCharArray())
    }

    fun preprocess(input: List<String>): Day05InputType1 {
        val stackSize = input.indexOfFirst { it[1] == '1' }
        val bucketCount = input.first { it[1] == '1' }.length / 4 + 1

        val crateStatus = mutableMapOf<Int, List<Char>>()
        for (stackPos in 1 .. bucketCount) {
            val stack = mutableListOf<Char>().also { crateStatus[stackPos] = it }
            for (pos in stackSize - 1 downTo 0) {
                val crate = input[pos].getOrNull(4 * (stackPos - 1) + 1)
                if (crate != null && crate.isLetter()) {
                    stack.add(crate)
                }
            }
        }

        val commands = input.subList(bucketCount + 1, input.size).filter { it != "" }.map {
            val split = it.split(" ")
            Triple(split[1].toInt(), split[3].toInt(), split[5].toInt())
        }

        return Pair(crateStatus, commands)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(5, true)
    check(part1(preprocess(testInput)) == "CMZ")

    val input = readInput(5)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == "MCD")
    println(part2(preprocess(input)))
}
