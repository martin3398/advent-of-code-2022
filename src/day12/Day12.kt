package day12

import readInput

fun Array<IntArray>.hasIndices(indices: Pair<Int, Int>) =
    indices.first >= 0 && indices.first < this.size && indices.second >= 0 && indices.second < this[indices.first].size

fun Array<IntArray>.get(indices: Pair<Int, Int>) = this[indices.first][indices.second]

fun Pair<Int, Int>.minusFirst(amount: Int = 1) = Pair(this.first - amount, this.second)
fun Pair<Int, Int>.plusFirst(amount: Int = 1) = Pair(this.first + amount, this.second)
fun Pair<Int, Int>.minusSecond(amount: Int = 1) = Pair(this.first, this.second - amount)
fun Pair<Int, Int>.plusSecond(amount: Int = 1) = Pair(this.first, this.second + amount)

fun main() {

    fun bfs(
        start: Pair<Int, Int>,
        map: Array<IntArray>,
        endCondition: (pos: Pair<Int, Int>, elevation: Int) -> Boolean
    ): Int {
        val visited = mutableSetOf(start)
        val queue = mutableListOf(Pair(0, start))
        while (queue.isNotEmpty()) {
            val (steps, next) = queue.removeFirst()
            if (endCondition(next, map.get(next))) {
                return steps
            }

            val toCheck = listOf(next.minusFirst(), next.plusFirst(), next.minusSecond(), next.plusSecond())
            for (check in toCheck) {
                if (map.hasIndices(check) && !visited.contains(check) && map.get(check) - map.get(next) <= 1) {
                    queue.add(Pair(steps + 1, check))
                    visited.add(check)
                }
            }
        }

        return Int.MAX_VALUE
    }

    fun part1(input: Triple<Pair<Int, Int>, Pair<Int, Int>, Array<IntArray>>): Int =
        bfs(input.first, input.third) { pos, _ ->
            pos == input.second
        }

    fun part2(input: Triple<Pair<Int, Int>, Pair<Int, Int>, Array<IntArray>>): Int = bfs(
        input.second,
        input.third.map { it.map { -it }.toIntArray() }.toTypedArray()
    ) { _, elevation ->
        elevation == 0
    }

    fun preprocess(input: List<String>): Triple<Pair<Int, Int>, Pair<Int, Int>, Array<IntArray>> {
        var start = Pair(0, 0)
        var end = Pair(0, 0)
        val map = input.mapIndexed { i, row ->
            row.mapIndexed { j, char ->
                when (char) {
                    'S' -> 'a'.code.also { start = Pair(i, j) }
                    'E' -> 'z'.code.also { end = Pair(i, j) }
                    else -> char.code
                } - 'a'.code
            }.toIntArray()
        }.toTypedArray()

        return Triple(start, end, map)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput(12, true)
    check(part1(preprocess(testInput)) == 31)

    val input = readInput(12)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 29)
    println(part2(preprocess(input)))
}
