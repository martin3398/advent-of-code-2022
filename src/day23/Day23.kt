package day23

import readInput


fun Pair<Int, Int>.surrounding() = listOf(
    first to second + 1,
    first to second - 1,
    first + 1 to second,
    first + 1 to second + 1,
    first + 1 to second - 1,
    first - 1 to second,
    first - 1 to second + 1,
    first - 1 to second - 1,
)

fun Pair<Int, Int>.north() = listOf(
    first - 1 to second,
    first - 1 to second + 1,
    first - 1 to second - 1,
)

fun Pair<Int, Int>.south() = listOf(
    first + 1 to second,
    first + 1 to second + 1,
    first + 1 to second - 1,
)

fun Pair<Int, Int>.west() = listOf(
    first to second - 1,
    first + 1 to second - 1,
    first - 1 to second - 1,
)

fun Pair<Int, Int>.east() = listOf(
    first to second + 1,
    first + 1 to second + 1,
    first - 1 to second + 1,
)

fun Pair<Int, Int>.toCheckOrder(round: Int): List<Pair<List<Pair<Int, Int>>, Pair<Int, Int>>> {
    val check = mutableListOf(
        north() to Pair(first - 1, second),
        south() to Pair(first + 1, second),
        west() to Pair(first, second - 1),
        east() to Pair(first, second + 1),
    )

    repeat(round % 4) {
        check.add(check.removeFirst())
    }

    check.add(0, surrounding() to this)
    check.add(listOf<Pair<Int, Int>>() to this)

    return check
}

fun main() {

    fun simulate(input: Set<Pair<Int, Int>>, round: Int): Pair<Set<Pair<Int, Int>>, Boolean> {
        val moves = mutableMapOf<Pair<Int, Int>, Pair<Int, Int>>()
        val count = mutableMapOf<Pair<Int, Int>, Int>()
        input.forEach { elf ->
            val check = elf.toCheckOrder(round)

            for ((toCheck, target) in check) {
                if (toCheck.all { !input.contains(it) }) {
                    moves[elf] = target
                    count[target] = (count[target] ?: 0) + 1
                    break
                }
            }
        }

        var changed = false
        val positions = mutableSetOf<Pair<Int, Int>>()
        moves.forEach { (source, target) ->
            if ((count[target] ?: 0) >= 2) {
                positions.add(source)
            } else {
                positions.add(target)
                if (source != target) {
                    changed = true
                }
            }
        }

        return positions to changed
    }

    fun part1(input: Set<Pair<Int, Int>>): Int {
        var positions = input
        repeat(10) {
            positions = simulate(positions, it).first
        }

        val sizeX = positions.maxOf { it.first } - positions.minOf { it.first } + 1
        val sizeY = positions.maxOf { it.second } - positions.minOf { it.second } + 1

        return sizeX * sizeY - positions.size
    }

    fun part2(input: Set<Pair<Int, Int>>): Int {
        var positions = input
        var round = 0
        while (true) {
            val changed = simulate(positions, round).also { positions = it.first }.second
            round++
            if (!changed) {
                return round
            }
        }
    }

    fun preprocess(input: List<String>): Set<Pair<Int, Int>> {
        val res = mutableSetOf<Pair<Int, Int>>()
        input.forEachIndexed { x, row ->
            row.forEachIndexed { y, c ->
                if (c == '#') {
                    res.add(x to y)
                }
            }
        }

        return res
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(23, true)
    check(part1(preprocess(testInput)) == 110)

    val input = readInput(23)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 20)
    println(part2(preprocess(input)))
}
