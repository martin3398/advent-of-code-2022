package day10

import readInput
import java.lang.Math.abs

sealed interface Instruction

object Noop : Instruction
data class AddX(val count: Int) : Instruction

fun main() {

    fun simulate(instructions: List<Instruction>, callback: (cycle: Int, registerValue: Int) -> Unit) {
        var cycle = 1
        var registerValue = 1

        instructions.forEach {
            when {
                it is Noop -> callback(cycle, registerValue).also { cycle++ }
                it is AddX -> {
                    callback(cycle, registerValue).also { cycle++ }
                    callback(cycle, registerValue).also { cycle++ }
                    registerValue += it.count
                }
            }
        }
    }

    fun part1(input: List<Instruction>): Int {
        var signalStrengthSum = 0

        simulate(input) { cycle, registerValue ->
            if ((cycle - 20) % 40 == 0)
                signalStrengthSum += cycle * registerValue
        }

        return signalStrengthSum
    }

    fun part2(input: List<Instruction>): String {
        val sb = StringBuilder()

        simulate(input) { cycle, registerValue ->
            val positionToDraw = (cycle - 1) % 40

            if (positionToDraw == 0) {
                sb.append("\n")
            }

            sb.append(if (abs(registerValue - positionToDraw) <= 1) "#" else " ")
        }

        return sb.toString()
    }

    fun preprocess(input: List<String>): List<Instruction> =
        input.map { if (it == "noop") Noop else AddX(it.split(" ").last().toInt()) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(10, true)
    check(part1(preprocess(testInput)) == 13140)

    val input = readInput(10)
    println(part1(preprocess(input)))

    println(part2(preprocess(testInput)))

    println(part2(preprocess(input)))
}
