package day11

import readInput

class Monkey(
    private val items: MutableList<Long>,
    private val operation: String,
    private val testDivisor: Long,
    private val trueTargetMonkey: Int,
    private val falseTargetMonkey: Int,
    private val modulo: Int
) {
    private var inspectionCount = 0

    fun inspectNext(divisor: Int = 3): Pair<Int, Long> {
        inspectionCount++

        val worryLevel = performOperation(items.removeFirst()) / divisor % modulo
        val target = if (worryLevel % testDivisor == 0L) trueTargetMonkey else falseTargetMonkey

        return Pair(target, worryLevel)
    }

    private fun performOperation(item: Long): Long {
        val operator = operation[4]
        val operandRaw = operation.split(" ").last()
        val operand = if (operandRaw == "old") item else operandRaw.toLong()

        return if (operator == '*') item * operand else item + operand
    }

    fun hasNext() = items.isNotEmpty()

    fun addItem(item: Long) = items.add(item)

    fun getInspectionCount() = inspectionCount
}

fun main() {
    fun simulate(input: List<Monkey>, count: Int, divisor: Int) = repeat(count) {
        input.forEach {
            while (it.hasNext()) {
                val (next, item) = it.inspectNext(divisor)
                input[next].addItem(item)
            }
        }
    }

    fun getMax2Product(input: List<Monkey>): Long {
        val inspectionCounts = input.map { it.getInspectionCount() }.toMutableList()
        val max1 = inspectionCounts.max().also { inspectionCounts.remove(it) }
        val max2 = inspectionCounts.max()

        return max1.toLong() * max2.toLong()
    }

    fun part1(input: List<Monkey>) = simulate(input, 20, 3).run { getMax2Product(input) }

    fun part2(input: List<Monkey>) = simulate(input, 10000, 1).run { getMax2Product(input) }

    fun preprocess(input: List<String>): List<Monkey> {
        val modulo = input.indices.step(7).map { input[it + 3].split(" ").last().toInt() }.reduce { acc, e -> acc * e }

        return input.indices.step(7).map { i ->
            Monkey(
                input[i + 1]
                    .split(" ")
                    .filter { it.firstOrNull()?.isDigit() == true }
                    .map { it.removeSuffix(",").toLong() }
                    .toMutableList(),
                input[i + 2].substring(19),
                input[i + 3].split(" ").last().toLong(),
                input[i + 4].split(" ").last().toInt(),
                input[i + 5].split(" ").last().toInt(),
                modulo
            )
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(11, true)
    check(part1(preprocess(testInput)) == 10605L)

    val input = readInput(11)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 2713310158L)
    println(part2(preprocess(input)))
}
