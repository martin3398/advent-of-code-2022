typealias Day03InputType1 = List<Pair<Set<Char>, Set<Char>>>
typealias Day03InputType2 = List<Set<Char>>

fun main() {

    fun getCommonItem(fst: Set<Char>, snd: Set<Char>): Char = fst.intersect(snd).first()

    fun getCommonItem(fst: Set<Char>, snd: Set<Char>, trd: Set<Char>): Char = fst.intersect(snd).intersect(trd).first()

    fun getPriority(c: Char): Int = if (c.isLowerCase()) {
        c - 'a' + 1
    } else {
        c - 'A' + 26 + 1
    }

    fun part1(input: Day03InputType1): Int = input.sumOf {
        getPriority(getCommonItem(it.first, it.second))
    }

    fun part2(input: Day03InputType2): Int {
        var total = 0
        for (i in input.indices step 3) {
            total += getPriority(getCommonItem(input[i], input[i+1], input[i+2]))
        }
        return total
    }

    fun preprocess1(input: List<String>): Day03InputType1 = input.map {
        Pair(it.substring(0, it.length / 2).toSet(), it.substring(it.length / 2).toSet())
    }

    fun preprocess2(input: List<String>): Day03InputType2 = input.map {
        it.toSet()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(3, true)
    check(part1(preprocess1(testInput)) == 157)

    val input = readInput(3)
    println(part1(preprocess1(input)))

    check(part2(preprocess2(testInput)) == 70)
    println(part2(preprocess2(input)))
}
