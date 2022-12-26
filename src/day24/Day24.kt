package day24

import readInput

enum class Direction(private val offset: Pair<Int, Int>) {
    UP(-1 to 0), DOWN(1 to 0), LEFT(0 to -1), RIGHT(0 to 1);

    fun transform(pos: Pair<Int, Int>) = Pair(pos.first + offset.first, pos.second + offset.second)

    companion object {
        fun fromChar(c: Char) = when (c) {
            '>' -> RIGHT
            '<' -> LEFT
            '^' -> UP
            'v' -> DOWN
            else -> error("Should not happen")
        }
    }
}

fun Array<BooleanArray>.setIfPossible(pos: Pair<Int, Int>, value: Boolean) {
    if (pos.first in indices && pos.second in this[pos.first].indices) {
        this[pos.first][pos.second] = value
    }
}

fun Pair<Int, Int>.wrap(maxFst: Int, maxSnd: Int): Pair<Int, Int> =
    Pair((first + maxFst) % maxFst, (second + maxSnd) % maxSnd)

class Basin(
    private var basin: Array<BooleanArray>,
    private var blizzards: List<Pair<Pair<Int, Int>, Direction>>
) {
    fun move() {
        movePlayers()
        moveBlizzards()
        removePlayers()
    }

    private fun movePlayers() {
        val basinNew = basin.map { it.copyOf() }.toTypedArray()
        basin.forEachIndexed { x, row ->
            row.forEachIndexed { y, pos ->
                if (pos) {
                    basinNew.setIfPossible(x - 1 to y, true)
                    basinNew.setIfPossible(x + 1 to y, true)
                    basinNew.setIfPossible(x to y - 1, true)
                    basinNew.setIfPossible(x to y + 1, true)
                }
            }
        }
        basin = basinNew
    }

    fun moveBlizzards() {
        blizzards = blizzards.map {
            it.second.transform(it.first).wrap(basin.size, basin[0].size) to it.second
        }
    }

    private fun removePlayers() {
        blizzards.forEach {
            basin[it.first.first][it.first.second] = false
        }
    }

    fun isSet(pos: Pair<Int, Int>) = basin[pos.first][pos.second]

    fun set(pos: Pair<Int, Int>) {
        basin[pos.first][pos.second] = true
    }

    fun clear() {
        basin = Array(basin.size) { BooleanArray(basin[0].size) { false } }
    }
}

fun main() {

    fun moveTo(basin: Basin, start: Pair<Int, Int>, end: Pair<Int, Int>): Int {
        basin.clear()
        basin.set(start)
        var minute = 1
        while (true) {
            basin.move()
            basin.set(start)
            minute++
            if (basin.isSet(end)) {
                return minute + 1
            }
        }
    }

    fun part1(input: Pair<Basin, Pair<Int, Int>>): Int {
        val (basin, end) = input
        basin.moveBlizzards()

        return moveTo(basin, 0 to 0, end)
    }

    fun part2(input: Pair<Basin, Pair<Int, Int>>): Int {
        val (basin, end) = input
        basin.moveBlizzards()

        var minutes = moveTo(basin, 0 to 0, end)
        repeat(2) { basin.moveBlizzards() }
        minutes += moveTo(basin, end, 0 to 0)
        repeat(2) { basin.moveBlizzards() }
        minutes += moveTo(basin, 0 to 0, end)

        return minutes
    }

    fun preprocess(input: List<String>): Pair<Basin, Pair<Int, Int>> {
        val basin = Array(input.size - 2) { BooleanArray(input[0].length - 2) { false } }
        basin[0][0] = true
        val blizzards = mutableListOf<Pair<Pair<Int, Int>, Direction>>()
        val end = basin.size - 1 to basin[0].size - 1

        for (x in 1 until input.size - 1) {
            for (y in 1 until input[x].length - 1) {
                if (input[x][y] != '.') {
                    blizzards.add((x - 1 to y - 1) to Direction.fromChar(input[x][y]))
                }
            }
        }

        return Basin(basin, blizzards) to end
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(24, true)
    check(part1(preprocess(testInput)) == 18)

    val input = readInput(24)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 54)
    println(part2(preprocess(input)))
}
