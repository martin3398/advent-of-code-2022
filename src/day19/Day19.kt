package day19

import readInput
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.ceil

data class Price(val ore: Int, val clay: Int, val obsidian: Int)

data class Blueprint(val orePrice: Price, val clayPrice: Price, val obsidianPrice: Price, val geodePrice: Price) {
    fun canBuyOreRobot(act: BfsEntry) = listOf(
        act.timeLeft > 0,
        orePrice.ore <= act.ore + act.oreRobots * act.timeLeft,
        act.oreRobots < listOf(orePrice.ore, clayPrice.ore, obsidianPrice.ore, geodePrice.ore).max(),
        act.oreRobots * act.timeLeft + act.ore <
                act.timeLeft * listOf(orePrice.ore, clayPrice.ore, obsidianPrice.ore, geodePrice.ore).max()
    ).all { it }

    fun canBuyClayRobot(act: BfsEntry) = listOf(
        act.timeLeft > 0,
        clayPrice.ore <= act.ore + act.oreRobots * act.timeLeft,
        act.clayRobots < obsidianPrice.clay,
        act.clayRobots * act.timeLeft + act.clay < act.timeLeft * obsidianPrice.clay
    ).all { it }

    fun canBuyObsidianRobot(act: BfsEntry) = listOf(
        act.timeLeft > 0,
        obsidianPrice.ore <= act.ore + act.oreRobots * act.timeLeft,
        obsidianPrice.clay <= act.clay + act.clayRobots * act.timeLeft,
        act.obsidianRobots < geodePrice.obsidian,
        act.obsidianRobots * act.timeLeft + act.obsidian < act.timeLeft * geodePrice.obsidian,
    ).all { it }

    fun canBuyGeodeRobot(act: BfsEntry) = listOf(
        act.timeLeft > 0,
        geodePrice.ore <= act.ore + act.oreRobots * act.timeLeft,
        geodePrice.obsidian <= act.obsidian + act.obsidianRobots * act.timeLeft
    ).all { it }

    fun buyOreRobot(act: BfsEntry): BfsEntry {
        val necessaryTime = max(ceil((orePrice.ore - act.ore).toDouble() / act.oreRobots).toInt() + 1, 1)
        return BfsEntry(
            act.timeLeft - necessaryTime,
            act.ore + necessaryTime * act.oreRobots - orePrice.ore,
            act.clay + necessaryTime * act.clayRobots,
            act.obsidian + necessaryTime * act.obsidianRobots,
            act.geode + necessaryTime * act.geodeRobots,
            act.oreRobots + 1,
            act.clayRobots,
            act.obsidianRobots,
            act.geodeRobots
        )
    }

    fun buyClayRobot(act: BfsEntry): BfsEntry {
        val necessaryTime = max(ceil((clayPrice.ore - act.ore).toDouble() / act.oreRobots).toInt() + 1, 1)
        return BfsEntry(
            act.timeLeft - necessaryTime,
            act.ore + necessaryTime * act.oreRobots - clayPrice.ore,
            act.clay + necessaryTime * act.clayRobots,
            act.obsidian + necessaryTime * act.obsidianRobots,
            act.geode + necessaryTime * act.geodeRobots,
            act.oreRobots,
            act.clayRobots + 1,
            act.obsidianRobots,
            act.geodeRobots,
        )
    }

    fun buyObsidianRobot(act: BfsEntry): BfsEntry {
        val necessaryTime = listOf(
            ceil((obsidianPrice.ore - act.ore).toDouble() / act.oreRobots).toInt() + 1,
            ceil((obsidianPrice.clay - act.clay).toDouble() / act.clayRobots).toInt() + 1,
            1
        ).max()
        return BfsEntry(
            act.timeLeft - necessaryTime,
            act.ore + necessaryTime * act.oreRobots - obsidianPrice.ore,
            act.clay + necessaryTime * act.clayRobots - obsidianPrice.clay,
            act.obsidian + necessaryTime * act.obsidianRobots,
            act.geode + necessaryTime * act.geodeRobots,
            act.oreRobots,
            act.clayRobots,
            act.obsidianRobots + 1,
            act.geodeRobots,
        )
    }

    fun buyGeodeRobot(act: BfsEntry): BfsEntry {
        val necessaryTime = listOf(
            ceil((geodePrice.ore - act.ore).toDouble() / act.oreRobots).toInt() + 1,
            ceil((geodePrice.obsidian - act.obsidian).toDouble() / act.obsidianRobots).toInt() + 1,
            1
        ).max()
        return BfsEntry(
            act.timeLeft - necessaryTime,
            act.ore + necessaryTime * act.oreRobots - geodePrice.ore,
            act.clay + necessaryTime * act.clayRobots,
            act.obsidian + necessaryTime * act.obsidianRobots - geodePrice.obsidian,
            act.geode + necessaryTime * act.geodeRobots,
            act.oreRobots,
            act.clayRobots,
            act.obsidianRobots,
            act.geodeRobots + 1,
        )
    }

}

data class BfsEntry(
    val timeLeft: Int = 24,
    val ore: Int = 0,
    val clay: Int = 0,
    val obsidian: Int = 0,
    val geode: Int = 0,
    val oreRobots: Int = 1,
    val clayRobots: Int = 0,
    val obsidianRobots: Int = 0,
    val geodeRobots: Int = 0,
)

fun main() {
    fun getMaxMinedGeode(blueprint: Blueprint, maxTime: Int = 24): Int {
        val queue = mutableListOf(BfsEntry(maxTime))

        var max = Int.MIN_VALUE
        while (queue.isNotEmpty()) {
            val act = queue.removeFirst()
            var build = false
            if (blueprint.canBuyGeodeRobot(act)) {
                queue.add(0, blueprint.buyGeodeRobot(act))
                build = true
            }
            if (blueprint.canBuyOreRobot(act)) {
                queue.add(0, blueprint.buyOreRobot(act))
                build = true
            }
            if (blueprint.canBuyClayRobot(act)) {
                queue.add(0, blueprint.buyClayRobot(act))
                build = true
            }
            if (blueprint.canBuyObsidianRobot(act)) {
                queue.add(0, blueprint.buyObsidianRobot(act))
                build = true
            }
            if (!build) {
                val geodeCount = act.geode + act.timeLeft * act.geodeRobots
                if (geodeCount > max) {
                    max = geodeCount
                }
            }
        }

        return max
    }

    fun part1(input: List<Blueprint>): Int {
        return input.mapIndexed { index, blueprint -> (index + 1) * getMaxMinedGeode(blueprint) }.sum()
    }

    fun part2(input: List<Blueprint>): Int {
        return input.slice(0..min(2, input.size - 1)).map { getMaxMinedGeode(it, 32) }.reduce { acc, e -> acc * e }
    }

    fun preprocess(input: List<String>): List<Blueprint> = input.map { row ->
        val split = row.split(":")[1].split(".").map { it.split(" ") }

        Blueprint(
            Price(split[0][5].toInt(), 0, 0),
            Price(split[1][5].toInt(), 0, 0),
            Price(split[2][5].toInt(), split[2][8].toInt(), 0),
            Price(split[3][5].toInt(), 0, split[3][8].toInt()),
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(19, true)
    check(part1(preprocess(testInput)) == 33)

    val input = readInput(19)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 3472)
    println(part2(preprocess(input)))
}
