package day09

import readInput
import kotlin.math.absoluteValue
import kotlin.math.sign

data class Position(val x: Int, val y: Int) {
    fun move(direction: Char): Position {
        return when (direction) {
            'U' -> Position(x, y + 1)
            'D' -> Position(x, y - 1)
            'R' -> Position(x + 1, y)
            'L' -> Position(x - 1, y)
            else -> throw IllegalArgumentException("Illegal Argument '$direction'")
        }
    }

    fun moveTowards(other: Position): Position {
        return if ((other.x - this.x).absoluteValue >= 2 || (other.y - this.y).absoluteValue >= 2) {
            Position(x + (other.x - this.x).sign, y + (other.y - this.y).sign)
        } else {
            this
        }
    }
}

fun main() {
    fun part1(input: List<Pair<Char, Int>>): Int {
        var head = Position(0, 0)
        var tail = Position(0, 0)

        val positions = mutableSetOf(tail)

        for (command in input) {
            for (i in 0 until command.second) {
                head = head.move(command.first)
                tail = tail.moveTowards(head)
                positions.add(tail)
            }
        }

        return positions.size
    }

    fun part2(input: List<Pair<Char, Int>>, size: Int = 10): Int {
        val knots = Array(size) { Position(0, 0) }

        val positions = mutableSetOf(knots[size - 1])

        for (command in input) {
            for (i in 0 until command.second) {
                knots[0] = knots[0].move(command.first)
                for (knotIndex in 1 until size) {
                    knots[knotIndex] = knots[knotIndex].moveTowards(knots[knotIndex - 1])
                }
                positions.add(knots[size - 1])
            }
        }

        return positions.size
    }

    fun preprocess(input: List<String>): List<Pair<Char, Int>> =
        input.map {
            val split = it.split(" ")
            Pair(it[0], split.last().toInt())
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(9, true)
    check(part1(preprocess(testInput)) == 88)

    val input = readInput(9)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 36)
    println(part2(preprocess(input)))
}
