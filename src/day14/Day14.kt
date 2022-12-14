package day14

import readInput
import java.lang.Integer.max
import java.lang.Integer.min

enum class Point {
    AIR,
    ROCK,
    SAND;
}

fun Array<Array<Point>>.get(pos: Pair<Int, Int>) = this[pos.first][pos.second]
fun Array<Array<Point>>.set(pos: Pair<Int, Int>, value: Point) {
    this[pos.first][pos.second] = value
}

fun Pair<Int, Int>.down() = Pair(this.first, this.second + 1)
fun Pair<Int, Int>.diagLeft() = Pair(this.first - 1, this.second + 1)
fun Pair<Int, Int>.diagRight() = Pair(this.first + 1, this.second + 1)

fun Array<Array<Point>>.moveSandStep(pos: Pair<Int, Int>): Pair<Int, Int> {
    val newPos = when (Point.AIR) {
        this.get(pos.down()) -> pos.down()
        this.get(pos.diagLeft()) -> pos.diagLeft()
        this.get(pos.diagRight()) -> pos.diagRight()
        else -> pos
    }

    this.set(pos, Point.AIR)
    this.set(newPos, Point.SAND)

    return newPos
}

fun Array<Array<Point>>.insertSand(insertPos: Pair<Int, Int> = Pair(500, 0)): Pair<Int, Int> {
    this.set(insertPos, Point.SAND)
    var lastPos: Pair<Int, Int>? = null
    var pos = insertPos
    while (pos != lastPos) {
        lastPos = pos
        pos = this.moveSandStep(pos)
    }
    return pos
}

fun main() {

    fun part1(input: Array<Array<Point>>): Int {
        var count = 0
        var pos = Pair(0, 0)
        while (pos.second < input[0].size - 2) {
            pos = input.insertSand()
            count++
        }

        return count - 1
    }

    fun part2(input: Array<Array<Point>>): Int {
        var count = 0
        while (input[500][0] == Point.AIR) {
            input.insertSand()
            count++
        }

        return count
    }

    fun preprocess(input: List<String>): Array<Array<Point>> {
        val inputTransformed: List<List<Pair<Int, Int>>> = input.map {
            it.split(" -> ").map {
                val split = it.split(",")
                Pair(split[0].toInt(), split[1].toInt())
            }
        }

        val maxY = inputTransformed.flatten().maxOf { it.second } + 2
        val maxX = inputTransformed.flatten().maxOf { it.first } + maxY + 2

        val result = Array(maxX + 1) { Array(maxY + 1) { if (it == maxY) Point.ROCK else Point.AIR } }

        for (path in inputTransformed) {
            var lastPoint: Pair<Int, Int> = path[0]
            for (point in path.subList(1, path.size)) {
                for (x in min(lastPoint.first, point.first)..max(lastPoint.first, point.first)) {
                    for (y in min(lastPoint.second, point.second)..max(lastPoint.second, point.second)) {
                        result[x][y] = Point.ROCK
                    }
                }

                lastPoint = point
            }
        }

        return result
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput(14, true)
    check(part1(preprocess(testInput)) == 24)

    val input = readInput(14)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 93)
    println(part2(preprocess(input)))
}
