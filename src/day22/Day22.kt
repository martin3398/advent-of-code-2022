package day22

import readInput

typealias InputType = Triple<Map<Pair<Int, Int>, Node>, List<Instruction>, Pair<Int, Int>>

enum class Direction(val score: Int) {
    N(3), S(1), W(2), E(0);

    fun rotate(direction: RotationDirection): Direction = when (direction) {
        RotationDirection.R -> rotateRight()
        RotationDirection.L -> rotateLeft()
    }

    private fun rotateLeft(): Direction = when (this) {
        S -> E
        W -> S
        N -> W
        E -> N
    }

    private fun rotateRight(): Direction = when (this) {
        N -> E
        E -> S
        S -> W
        W -> N
    }
}

abstract class Instruction
data class Rotate(val direction: RotationDirection) : Instruction()
data class Move(val steps: Int) : Instruction()

enum class RotationDirection { R, L }

data class Actor(
    val graph: Map<Pair<Int, Int>, Node>,
    var pos: Pair<Int, Int>,
    var facingDirection: Direction = Direction.E
) {
    fun move(steps: Int) {
        repeat(steps) {
            val (pos, facingDirection) = graph[pos]!!.move(facingDirection)
            this.pos = pos
            this.facingDirection = facingDirection
        }
    }

    fun rotate(direction: RotationDirection) {
        facingDirection = facingDirection.rotate(direction)
    }

    fun getScore(): Int = 1000 * (pos.first + 1) + 4 * (pos.second + 1) + facingDirection.score
}

data class Node(val neighbors: Map<Direction, Pair<Pair<Int, Int>, Direction>>, val pos: Pair<Int, Int>) {
    fun move(direction: Direction): Pair<Pair<Int, Int>, Direction> =
        neighbors.getOrDefault(direction, pos to direction)
}

fun main() {

    fun solve(input: InputType): Int {
        val (graph, instructions, start) = input
        val actor = Actor(graph, start)
        instructions.forEach {
            when (it) {
                is Rotate -> actor.rotate(it.direction)
                is Move -> actor.move(it.steps)
            }
        }
        return actor.getScore()
    }

    fun wrapPart1(pos: Pair<Int, Int>, direction: Direction): Pair<Pair<Int, Int>, Direction> {
        val (y, x) = pos
        return when (direction) {
            Direction.N ->
                if (x in 0 until 50 && y == 100) (199 to x) to direction
                else if (x in 50 until 100 && y == 0) (149 to x) to direction
                else if (x in 100 until 150 && y == 0) (49 to x) to direction
                else (y - 1 to x) to direction

            Direction.S ->
                if (x in 0 until 50 && y == 199) (100 to x) to direction
                else if (x in 50 until 100 && y == 149) (0 to x) to direction
                else if (x in 100 until 150 && y == 49) (0 to x) to direction
                else (y + 1 to x) to direction

            Direction.W ->
                if (y in 0 until 50 && x == 50) (y to 149) to direction
                else if (y in 50 until 100 && x == 50) (y to 99) to direction
                else if (y in 100 until 150 && x == 0) (y to 99) to direction
                else if (y in 150 until 200 && x == 0) (y to 49) to direction
                else (y to x - 1) to direction

            Direction.E ->
                if (y in 0 until 50 && x == 149) (y to 50) to direction
                else if (y in 50 until 100 && x == 99) (y to 50) to direction
                else if (y in 100 until 150 && x == 99) (y to 0) to direction
                else if (y in 150 until 200 && x == 49) (y to 0) to direction
                else (y to x + 1) to direction
        }
    }

    fun wrapPart2(pos: Pair<Int, Int>, direction: Direction): Pair<Pair<Int, Int>, Direction> {
        val (y, x) = pos
        return when (direction) {
            Direction.N ->
                if (x in 0 until 50 && y == 100) (50 + x to 50) to Direction.E
                else if (x in 50 until 100 && y == 0) (150 + (x - 50) to 0) to Direction.E
                else if (x in 100 until 150 && y == 0) (199 to x - 100) to Direction.N
                else (y - 1 to x) to direction

            Direction.S ->
                if (x in 0 until 50 && y == 199) (0 to x + 100) to Direction.S
                else if (x in 50 until 100 && y == 149) (150 + x - 50 to 49) to Direction.W
                else if (x in 100 until 150 && y == 49) (50 + x - 100 to 99) to Direction.W
                else (y + 1 to x) to direction

            Direction.W ->
                if (y in 0 until 50 && x == 50) (149 - y to 0) to Direction.E
                else if (y in 50 until 100 && x == 50) (100 to y - 50) to Direction.S
                else if (y in 100 until 150 && x == 0) (49 - (y - 100) to 50) to Direction.E
                else if (y in 150 until 200 && x == 0) (0 to 50 + (y - 150)) to Direction.S
                else (y to x - 1) to direction

            Direction.E ->
                if (y in 0 until 50 && x == 149) (149 - y to 99) to Direction.W
                else if (y in 50 until 100 && x == 99) (49 to 100 + (y - 50)) to Direction.N
                else if (y in 100 until 150 && x == 99) (49 - (y - 100) to 149) to Direction.W
                else if (y in 150 until 200 && x == 49) (149 to 50 + (y - 150)) to Direction.N
                else (y to x + 1) to direction
        }
    }

    fun preprocess(
        input: List<String>,
        wrapper: (Pair<Int, Int>, Direction) -> Pair<Pair<Int, Int>, Direction>
    ): InputType {
        val instructions = input.last().replace("R", ",R,").replace("L", ",L,").split(",").map {
            if (it == "L" || it == "R") {
                Rotate(RotationDirection.valueOf(it))
            } else {
                Move(it.toInt())
            }
        }

        val map = mutableMapOf<Pair<Int, Int>, Node>()
        val maxRow = input.slice(0 until input.size - 2).maxOf { it.length }
        val mapRaw = input.slice(0 until input.size - 2).map { it.padEnd(maxRow) }
        for (x in mapRaw.indices) {
            for (y in mapRaw[x].indices) {
                if (mapRaw[x][y] == '.') {
                    val pos = x to y
                    val neighbors = Direction.values().associateWith { wrapper(pos, it) }
                        .filterValues { mapRaw[it.first.first][it.first.second] == '.' }


                    map[pos] = Node(neighbors, pos)
                }
            }
        }

        return Triple(map, instructions, Pair(0, input[0].indexOf('.')))
    }

    // test if implementation meets criteria from the description, like:
    val input = readInput(22)
    println(solve(preprocess(input) { pos, direction ->
        wrapPart1(pos, direction)
    }))

    println(solve(preprocess(input) { pos, direction ->
        wrapPart2(pos, direction)
    }))
}
