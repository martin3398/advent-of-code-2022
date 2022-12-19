package day17

import readInput

enum class UnitType(val repr: Char) {
    FALLING('@'),
    STOPPED('#'),
    AIR('.')
}

enum class Direction(val repr: Char) {
    DOWN('|'),
    LEFT('<'),
    RIGHT('>');

    companion object {
        fun fromRepr(repr: Char) = values().toList().first { it.repr == repr }
    }
}

class Cave {
    private var cave: MutableList<MutableList<UnitType>> = MutableList(4) { MutableList(7) { UnitType.AIR } }

    fun insert(shape: Int) {
        val nextInsert = (largestIndexNullable() ?: -1) + 3
        val upperRow = nextInsert + shapeSize(shape)
        while (cave.size <= upperRow) {
            cave.add(MutableList(7) { UnitType.AIR })
        }
        when (shape % 5) {
            0 -> for (i in 0..3) {
                cave[upperRow][2 + i] = UnitType.FALLING
            }

            1 -> for (i in 0..2) {
                cave[upperRow - 1][2 + i] = UnitType.FALLING
                cave[upperRow - i][2 + 1] = UnitType.FALLING
            }

            2 -> for (i in 0..2) {
                cave[upperRow - 2][2 + i] = UnitType.FALLING
                cave[upperRow - i][2 + 2] = UnitType.FALLING
            }

            3 -> for (i in 0..3) {
                cave[upperRow - i][2] = UnitType.FALLING
            }

            4 -> for (i in 0..1) {
                for (j in 0..1) {
                    cave[upperRow - i][2 + j] = UnitType.FALLING
                }
            }
        }
    }

    fun largestIndex(): Int = largestIndexNullable()!!

    private fun largestIndexNullable(): Int? =
        cave.indexOfLast { row -> row.any { it != UnitType.AIR } }.let { if (it == -1) null else it }

    private fun shapeSize(index: Int): Int = when (index % 5) {
        0 -> 1
        1 -> 3
        2 -> 3
        3 -> 4
        4 -> 2
        else -> throw IllegalStateException()
    }

    fun move(direction: Direction): Boolean {
        moveSingle(direction)

        val moved = moveSingle(Direction.DOWN)
        if (!moved) {
            fixate()
        }
        return moved
    }

    private fun fixate() {
        cave =
            cave.map { row -> row.map { if (it == UnitType.FALLING) UnitType.STOPPED else it }.toMutableList() }
                .toMutableList()
    }

    private fun height(pos: Int): Int {
        for (i in cave.indices) {
            if (cave[cave.size - i - 1][pos] == UnitType.STOPPED) {
                return i
            }
        }
        return -1
    }

    fun heightRepresentation(): String = (0..6).map { height(it) }.joinToString { "$it." }

    private fun moveSingle(direction: Direction): Boolean {
        val caveNew =
            MutableList(cave.size) { i -> MutableList(7) { j -> if (cave[i][j] == UnitType.FALLING) UnitType.AIR else cave[i][j] } }

        cave.forEachIndexed { i, row ->
            row.forEachIndexed { j, unit ->
                if (unit == UnitType.FALLING) {
                    val x = j + when (direction) {
                        Direction.DOWN -> 0
                        Direction.LEFT -> -1
                        Direction.RIGHT -> 1
                    }
                    val y = i + if (direction == Direction.DOWN) -1 else 0

                    if (x < 0 || x >= cave[0].size || y < 0 || y >= cave.size || caveNew[y][x] != UnitType.AIR) {
                        return false
                    }

                    caveNew[y][x] = UnitType.FALLING
                }
            }
        }

        cave = caveNew
        return true
    }

    override fun toString(): String {
        return cave
            .reversed()
            .map { row -> "|" + String(row.map { it.repr }.toCharArray()) + "|" }
            .toMutableList()
            .also { it.add("+-------+\n") }
            .joinToString("\n")
    }
}

fun main() {

    fun moveRock(cave: Cave, input: CharArray, shape: Long, i: Int): Int {
        var iMut = i
        cave.insert((shape % 5).toInt())
        var moved = true
        while (moved) {
            moved = cave.move(Direction.fromRepr(input[iMut]))
            iMut = (iMut + 1) % input.size
        }

        return iMut
    }

    fun simulate(input: CharArray, iterations: Long = 1000000000000L): Long {
        val cave = Cave()
        val hashes = mutableMapOf<Int, Pair<Long, Int>>()

        var height = 0L
        var jetPos = 0

        var iter = 0L
        while (iter < iterations) {
            jetPos = moveRock(cave, input, iter, jetPos)

            val hash = "${cave.heightRepresentation()}.$jetPos.${iter % 5}".hashCode()
            if (hashes.containsKey(hash)) {
                val timeSpan = iter - hashes[hash]!!.first
                val heightSpan = cave.largestIndex() - hashes[hash]!!.second
                val skippedIterations = (iterations - iter - 1) / timeSpan

                iter += timeSpan * skippedIterations
                height += heightSpan * skippedIterations

                hashes.clear()
            } else {
                hashes[hash] = Pair(iter, cave.largestIndex())
            }
            iter++
        }

        return cave.largestIndex() + height + 1
    }

    fun part1(input: CharArray): Int = simulate(input, 2022L).toInt()

    fun part2(input: CharArray): Long = simulate(input, 1000000000000L)

    fun preprocess(input: List<String>): CharArray = input.first().toCharArray()


    // test if implementation meets criteria from the description, like:
    val testInput = readInput(17, true)
    check(part1(preprocess(testInput)) == 3068)

    val input = readInput(17)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 1514285714288L)
    println(part2(preprocess(input)))
}
