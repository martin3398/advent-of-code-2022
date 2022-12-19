package day16

import readInput

data class Node(val value: Int, val next: MutableList<String>)

data class BfsEntry(val position: String, val remainder: Int, val open: List<String>)

fun main() {

    fun floydWarshall(graph: Map<String, Node>): Map<String, Map<String, Int>> {
        val distances = mutableMapOf<String, MutableMap<String, Int>>()
        for (u in graph.keys) {
            distances[u] = mutableMapOf()
            for (v in graph.keys) {
                distances[u]!![v] = if (u == v) {
                    0
                } else if (graph[u]!!.next.contains(v) || graph[v]!!.next.contains(u)) {
                    1
                } else {
                    graph.keys.size + 1
                }
            }
        }

        for (i in graph.keys) for (j in graph.keys) for (k in graph.keys) {
            if (distances[i]!![k]!! + distances[k]!![j]!! <= distances[i]!![j]!!) {
                distances[i]?.set(j, distances[i]!![k]!! + distances[k]!![j]!!)
                distances[j]?.set(i, distances[i]!![k]!! + distances[k]!![j]!!)
            }
        }

        return distances
    }

    fun solutionGenerator(
        graph: Map<String, Node>,
        distances: Map<String, Map<String, Int>>,
        start: String,
        maxTime: Int,
        opened: Set<String> = setOf(),
        yieldAll: Boolean = false
    ) = sequence {
        val nodes: Set<String> = graph.filter { it.value.value > 0 }.filter { !opened.contains(it.key) }.keys

        val queue = mutableListOf(BfsEntry(start, maxTime, listOf()))
        while (queue.isNotEmpty()) {
            val (position, remainder, open) = queue.removeFirst()
            val successors = nodes.subtract(open.toSet()).filter { distances[position]?.get(it)!! + 1 <= remainder }
            if (yieldAll || successors.isEmpty()) {
                yield(open)
            }
            successors.forEach { next ->
                queue.add(BfsEntry(
                    next,
                    remainder - distances[position]?.get(next)!! - 1,
                    open.toMutableList().also { it.add(next) }
                ))
            }
        }
    }

    fun getReleasedPressure(
        graph: Map<String, Node>,
        distances: Map<String, Map<String, Int>>,
        open: List<String>,
        start: String,
        maxTime: Int
    ): Int {
        var position = start
        var remainingTime = maxTime
        var pressureReleased = 0
        for (next in open) {
            remainingTime -= distances[position]!![next]!! + 1
            pressureReleased += remainingTime * graph[next]!!.value
            position = next
        }

        return pressureReleased
    }

    fun part1(graph: Map<String, Node>, start: String = "AA", maxTime: Int = 30): Int {
        val distances = floydWarshall(graph)

        return solutionGenerator(graph, distances, start, maxTime).asIterable()
            .maxOf { getReleasedPressure(graph, distances, it, start, maxTime) }
    }

    fun part2(graph: Map<String, Node>, start: String = "AA", maxTime: Int = 26): Int {
        val distances = floydWarshall(graph)

        return solutionGenerator(graph, distances, start, maxTime, setOf(), true).asIterable()
            .maxOf {
                val protagonistReleased = getReleasedPressure(graph, distances, it, start, maxTime)
                val elephantReleased = solutionGenerator(graph, distances, start, maxTime, it.toSet()).asIterable()
                    .maxOf { elephant -> getReleasedPressure(graph, distances, elephant, start, maxTime) }

                protagonistReleased + elephantReleased
            }
    }

    fun preprocess(input: List<String>): Map<String, Node> =
        input.associate { inputStr ->
            val split = inputStr.split(" ")
            Pair(
                split[1], Node(
                    split[4].removePrefix("rate=").removeSuffix(";").toInt(),
                    split.subList(9, split.size).map { it.replace(',', ' ').trim() }.toMutableList()
                )
            )
        }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput(16, true)
    check(part1(preprocess(testInput)) == 1651)

    val input = readInput(16)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 1707)
    println(part2(preprocess(input)))
}
