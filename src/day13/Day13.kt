package day13

import org.json.JSONArray
import readInput
import java.lang.Integer.min


fun main() {

    fun compare(arr1: JSONArray, arr2: JSONArray): Int {
        for (i in 0 until min(arr1.length(), arr2.length())) {
            val e1 = arr1.get(i)
            val e2 = arr2.get(i)
            if (e1 is Int && e2 is Int) {
                if (e1 != e2) {
                    return e2 - e1
                } else {
                    continue
                }
            }

            if (e1 is JSONArray && e2 is JSONArray) {
                val order = compare(e1, e2)
                if (order != 0) {
                    return order
                } else {
                    continue
                }
            }

            if (e1 is Int && e2 is JSONArray) {
                val order = compare(JSONArray(listOf(e1)), e2)
                if (order != 0) {
                    return order
                } else {
                    continue
                }
            }

            if (e1 is JSONArray && e2 is Int) {
                val order = compare(e1, JSONArray(listOf(e2)))
                if (order != 0) {
                    return order
                } else {
                    continue
                }
            }
        }

        return arr2.length() - arr1.length()
    }

    val comparator = Comparator<JSONArray> { e1, e2 -> compare(e1, e2) }

    fun part1(input: List<Pair<JSONArray, JSONArray>>): Int = input.mapIndexed { index, pair ->
        if (compare(pair.first, pair.second) > 0) index + 1 else 0
    }.sum()

    fun part2(input: List<JSONArray>): Int {
        val fstDivider = JSONArray("[[2]]")
        val sndDivider = JSONArray("[[6]]")

        val sorted = input.toMutableList().also { it.add(fstDivider) }.also { it.add(sndDivider) }
            .sortedWith(comparator)
            .reversed()

        var res = 1
        sorted.forEachIndexed { index, jsonArray ->
            if (jsonArray == fstDivider || jsonArray == sndDivider) {
                res *= (index + 1)
            }
        }

        return res
    }

    fun parse(input: String): JSONArray = JSONArray(input)

    fun preprocess1(input: List<String>): List<Pair<JSONArray, JSONArray>> = input.indices.step(3).map {
        Pair(parse(input[it]), parse(input[it + 1]))
    }

    fun preprocess2(input: List<String>): List<JSONArray> = input.filter { it.isNotBlank() }.map { parse(it) }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput(13, true)
    check(part1(preprocess1(testInput)) == 13)

    val input = readInput(13)
    println(part1(preprocess1(input)))

    check(part2(preprocess2(testInput)) == 140)
    println(part2(preprocess2(input)))
}
