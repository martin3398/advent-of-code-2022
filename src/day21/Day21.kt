package day21

import readInput
import java.lang.IllegalStateException

enum class Operator(val repr: Char) {
    ADD('+'),
    SUB('-'),
    MULT('*'),
    DIV('/');

    fun apply(fst: Long, snd: Long): Long = when (this) {
        ADD -> fst + snd
        SUB -> fst - snd
        MULT -> fst * snd
        DIV -> fst / snd
    }

    fun reverseFst(fst: Long, res: Long): Long = when (this) {
        ADD -> res - fst
        SUB -> fst - res
        MULT -> res / fst
        DIV -> fst / res
    }

    fun reverseSnd(snd: Long, res: Long): Long = when (this) {
        ADD -> res - snd
        SUB -> snd + res
        MULT -> res / snd
        DIV -> res * snd
    }

    companion object {
        fun fromRepr(repr: Char): Operator = values().first { it.repr == repr }
    }
}

abstract class Monkey

data class Literal(val literal: Long) : Monkey()
data class Instruction(val fst: String, val operator: Operator, val snd: String) : Monkey()
class Me : Monkey()

fun main() {

    fun solveMap(input: MutableMap<String, Monkey>) {
        var changed = true
        while (changed) {
            changed = false
            for ((name, monkey) in input) {
                if (monkey is Instruction) {
                    val fst = input[monkey.fst]
                    val snd = input[monkey.snd]

                    if (fst is Literal && snd is Literal) {
                        input[name] = Literal(monkey.operator.apply(fst.literal, snd.literal))
                        changed = true
                    }
                }
            }
        }
    }

    fun part1(input: MutableMap<String, Monkey>): Long = solveMap(input).let { (input["root"] as Literal).literal }

    fun part2(input: MutableMap<String, Monkey>): Long {
        input["humn"] = Me()
        solveMap(input)

        fun getValueOfRev(monkey: Monkey, target: Long): Long {
            if (monkey is Me) {
                return target
            }
            if (monkey is Instruction) {
                val fst = input[monkey.fst]
                val snd = input[monkey.snd]

                if (fst is Literal) {
                    return getValueOfRev(snd!!, monkey.operator.reverseFst(fst.literal, target))
                }
                if (snd is Literal) {
                    return getValueOfRev(fst!!, monkey.operator.reverseSnd(snd.literal, target))
                }
            }

            throw IllegalStateException()
        }

        val root = input["root"] as Instruction
        val searchable = if(input[root.fst] is Literal) input[root.snd] else input[root.fst]
        val target = (if(input[root.fst] is Literal) input[root.fst] else input[root.snd]) as Literal

        return getValueOfRev(searchable as Instruction, target.literal)
    }

    fun preprocess(input: List<String>): MutableMap<String, Monkey> = input.associate {
        val split = it.split(" ")
        val literal = split[1].toLongOrNull()

        split[0].substring(0..3) to if (literal != null) Literal(literal) else Instruction(
            split[1],
            Operator.fromRepr(split[2][0]),
            split[3]
        )
    }.toMutableMap()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(21, true)
    check(part1(preprocess(testInput)) == 152L)

    val input = readInput(21)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 301L)
    println(part2(preprocess(input)))
}
