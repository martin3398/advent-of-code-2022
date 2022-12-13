package day08

import readInput

fun main() {
    fun part1(input: Array<IntArray>): Int {
        val width = input.size
        val height = input[0].size
        val visible = Array(height) {
            BooleanArray(width) {
                false
            }
        }

        for (i in 0 until height) {
            visible[i][0] = true
            visible[i][width - 1] = true
        }
        for (i in 0 until width) {
            visible[0][i] = true
            visible[height - 1][i] = true
        }

        var maximum: Int
        for (i in 1 until height - 1) {
            maximum = input[i][0]
            for (j in 1 until width - 1) {
                if (input[i][j] > maximum) {
                    maximum = input[i][j]
                    visible[i][j] = true
                }
            }
            maximum = input[i][width - 1]
            for (j in width - 2 downTo  1) {
                if (input[i][j] > maximum) {
                    maximum = input[i][j]
                    visible[i][j] = true
                }
            }
        }
        for (j in 1 until width - 1) {
            maximum = input[0][j]
            for (i in 1 until height - 1) {
                if (input[i][j] > maximum) {
                    maximum = input[i][j]
                    visible[i][j] = true
                }
            }
            maximum = input[height - 1][j]
            for (i in height - 2 downTo  1) {
                if (input[i][j] > maximum) {
                    maximum = input[i][j]
                    visible[i][j] = true
                }
            }
        }

        return visible.map { it.map { x -> if (x) 1 else 0 }.sum() }.sum()
    }

    fun part2(input: Array<IntArray>): Int {
        val width = input.size
        val height = input[0].size
        val viewingDistances = Array(height) {
            IntArray(width) {
                1
            }
        }

        for (i in 0 until height) {
            viewingDistances[i][0] = 0
            viewingDistances[i][width - 1] = 0
        }
        for (j in 0 until width) {
            viewingDistances[0][j] = 0
            viewingDistances[height - 1][j] = 0
        }

        for (i in 1 until height - 1) {
            for (j in 1 until width - 1) {
                var dist = 1
                while (j - dist > 0 && input[i][j] > input[i][j - dist]) {
                    dist++
                }
                viewingDistances[i][j] *= dist
                dist = 1
                while (j + dist < width - 1 && input[i][j] > input[i][j + dist]) {
                    dist++
                }
                viewingDistances[i][j] *= dist
                dist = 1
                while (i - dist > 0 && input[i][j] > input[i - dist][j]) {
                    dist++
                }
                viewingDistances[i][j] *= dist
                dist = 1
                while (i + dist < height - 1 && input[i][j] > input[i + dist][j]) {
                    dist++
                }
                viewingDistances[i][j] *= dist
            }
        }

        return viewingDistances.maxOf { it.max() }
    }

    fun preprocess(input: List<String>): Array<IntArray> =
        input.map { it.map { char -> char.digitToInt() }.toIntArray() }.toTypedArray()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(8, true)
    check(part1(preprocess(testInput)) == 21)

    val input = readInput(8)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 8)
    println(part2(preprocess(input)))
}
