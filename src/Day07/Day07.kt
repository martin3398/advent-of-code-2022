import kotlin.math.min

abstract class Node(var size: Int = 0)

class InnerNode(val parent: InnerNode?) : Node() {
    val successors: MutableMap<String, Node> = mutableMapOf()
}

class Leaf(size: Int): Node(size)

fun main() {

    fun buildTree(input: List<String>): InnerNode {
        val root = InnerNode(null)
        var cur = root
        var i = 1
        while (i < input.size) {
            var line = input[i]
            if (line == "\$ ls") {
                i++
                while (i < input.size && !input[i].startsWith("\$")) {
                    line = input[i]
                    val name = line.split(" ").last()
                    cur.successors[name] = if (line.startsWith("dir")) {
                        InnerNode(cur)
                    } else {
                        Leaf(line.split(" ").first().toInt())
                    }

                    i++
                }

                if (cur.successors.all { it.value is Leaf }) {
                    cur.size = cur.successors.values.sumOf { it.size }
                }

                continue
            } else if (line == "\$ cd ..") {
                cur = cur.parent!!

                cur.size = cur.successors.values.sumOf { it.size }
            } else {
                cur = cur.successors[line.split(" ").last()] as InnerNode
            }

            i++
        }

        while (cur != root) {
            cur = cur.parent!!

            cur.size = cur.successors.values.sumOf { it.size }
        }

        return root
    }

    fun getTotalSizeUnderThresh(node: Node, thresh: Int = 100000): Int {
        if (node is InnerNode) {
            return (if (node.size <= thresh) node.size else 0) + node.successors.values.sumOf { getTotalSizeUnderThresh(it, thresh) }
        }

        return 0
    }

    fun part1(input: List<String>): Int {
        val tree = buildTree(input)

        return getTotalSizeUnderThresh(tree)
    }

    fun findSmallestOverThresh(node: Node, thresh: Int): Int {
        if (node is InnerNode) {
            val successorMin = node.successors.values.minOf { findSmallestOverThresh(it, thresh) }
            if (node.size < thresh) {
                return successorMin
            }

            return min(successorMin, node.size)
        }

        return Int.MAX_VALUE
    }

    fun part2(input: List<String>): Int {
        val tree = buildTree(input)

        val free = 70000000 - tree.size
        val target = 30000000 - free

        return findSmallestOverThresh(tree, target)
    }

    fun preprocess(input: List<String>): List<String> = input

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(7, true)
    check(part1(preprocess(testInput)) == 95437)

    val input = readInput(7)
    println(part1(preprocess(input)))

    check(part2(preprocess(testInput)) == 24933642)
    println(part2(preprocess(input)))
}
