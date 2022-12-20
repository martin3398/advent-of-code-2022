package day18

import readInput

enum class Axis { X, Y, Z }

data class Side(val x: Int, val y: Int, val z: Int, val axis: Axis) {
    companion object {
        fun fromCoordinatesAndAxis(coordinates: Triple<Int, Int, Int>, axis: Axis): Side =
            Side(coordinates.first, coordinates.second, coordinates.third, axis)
    }
}

fun Triple<Int, Int, Int>.incrementAxis(axis: Axis): Triple<Int, Int, Int> = when (axis) {
    Axis.X -> Triple(first + 1, second, third)
    Axis.Y -> Triple(first, second + 1, third)
    Axis.Z -> Triple(first, second, third + 1)
}

class Cube {
    private val sides = mutableSetOf<Side>()
    private var duplicates = mutableSetOf<Side>()

    fun build(input: Collection<Triple<Int, Int, Int>>) = input.forEach {
        Axis.values().forEach { axis ->
            if (!sides.add(Side.fromCoordinatesAndAxis(it, axis))) duplicates.add(Side.fromCoordinatesAndAxis(it, axis))
            if (!sides.add(Side.fromCoordinatesAndAxis(it.incrementAxis(axis), axis)))
                duplicates.add(Side.fromCoordinatesAndAxis(it.incrementAxis(axis), axis))
        }
    }

    fun getAllSidesCount() = sides.size - duplicates.size

    fun intersect(other: Cube): Cube = Cube().also {
        it.sides.addAll(sides.intersect(other.sides))
        it.duplicates.addAll(duplicates.intersect(other.duplicates))
    }
}

fun Triple<Int, Int, Int>.isInBoundary(
    boundaryX: Pair<Int, Int>,
    boundaryY: Pair<Int, Int>,
    boundaryZ: Pair<Int, Int>
) = first >= boundaryX.first && first <= boundaryX.second
        && second >= boundaryY.first && second <= boundaryY.second
        && third >= boundaryZ.first && third <= boundaryZ.second


fun Triple<Int, Int, Int>.neighbors() = listOf(
    Triple(first + 1, second, third),
    Triple(first - 1, second, third),
    Triple(first, second + 1, third),
    Triple(first, second - 1, third),
    Triple(first, second, third + 1),
    Triple(first, second, third - 1),
)


fun main() {

    fun part1(input: List<Triple<Int, Int, Int>>): Int {
        return Cube().also { it.build(input) }.getAllSidesCount()
    }

    fun part2(input: List<Triple<Int, Int, Int>>): Int {
        val cube = Cube().also { it.build(input) }

        val boundaryX = Pair(input.minOf { it.first } - 1, input.maxOf { it.first } + 1)
        val boundaryY = Pair(input.minOf { it.second } - 1, input.maxOf { it.second } + 1)
        val boundaryZ = Pair(input.minOf { it.third } - 1, input.maxOf { it.third } + 1)

        val visited = mutableListOf<Triple<Int, Int, Int>>()
        val queue = mutableListOf(Triple(boundaryX.first, boundaryY.first, boundaryZ.first))
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()

            for (e in next.neighbors()) {
                if (e.isInBoundary(boundaryX, boundaryY, boundaryZ) && !visited.contains(e) && !input.contains(e)) {
                    queue.add(e)
                    visited.add(e)
                }
            }
        }

        val cubeOutside = Cube().also { it.build(visited) }
        val intersected = cube.intersect(cubeOutside)

        return intersected.getAllSidesCount()
    }

    fun preprocess(input: List<String>): List<Triple<Int, Int, Int>> = input.map { row ->
        val split = row.split(",").map { it.toInt() }
        Triple(split[0], split[1], split[2])
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(18, true)
    check(part1(preprocess(testInput)) == 64)

    val input = readInput(18)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 58)
    println(part2(preprocess(input)))
}
