package day08

import println
import readInput
import lcm

class Node(val value: String, val left: String, val right: String)
class NodeCycle(var firstZ: Long = 0, var secondZ: Long = 0, var thirdZ: Long = 0) {
    fun occurence(findAt: Long) {
        if (firstZ == 0L) {
            firstZ = findAt
        } else if (secondZ == 0L) {
            secondZ = findAt
        } else if (thirdZ == 0L) {
            thirdZ = findAt
        }
    }
    fun cycleLength(): Long {
        return secondZ - firstZ
    }

    fun isComplete(): Boolean {
        return firstZ > 0 && secondZ > 0 && thirdZ > 0
    }
}

fun MutableList<NodeCycle>.lcm(): Long {
    var result: Long = this.first().cycleLength() / this.count()
    for ((index, node) in this.withIndex()) {
        "NODE: ${node.cycleLength()}".println()
        result = lcm(result, node.cycleLength() / this.count())
        "ReSULT: $result".println()
    }
    return result
}

fun MutableList<NodeCycle>.max(): Long {
    var max: Long = Long.MIN_VALUE

    for (node in this) {
        max = kotlin.math.max(max, node.cycleLength())
    }
    return max
}

fun main() {
    val directionR = """(\w)""".toRegex()
    val nodeR = """(.*) = \((.*), (.*)\)""".toRegex()
    var directions: Sequence<String> = emptySequence()
    val nodeList: MutableList<Node> = emptyList<Node>().toMutableList()

    fun part1(input: List<String>): Int {
        for ((index, string) in input.withIndex()) {
            if (index == 0) {
                directions = directionR.findAll(string).map { it.value }
            } else if (index > 1) {
               val (value, left, right) = nodeR.matchEntire(string)
                    ?.destructured
                    ?: throw IllegalArgumentException("Incorrect input line")

                nodeList.add(Node(value, left, right))
            }
        }

        var steps = 0
        var currentNode = nodeList.first{ it.value == "AAA" }
        while (true) {
            for (direction in directions) {
               if (direction == "L") {
                    currentNode = nodeList.first { it.value == currentNode.left }
               } else {
                   currentNode = nodeList.first { it.value == currentNode.right }
               }
                steps++
                if (currentNode.value == "ZZZ") {
                    return steps
                }
            }
        }
    }

    fun part2(input: List<String>): Long {
        for ((index, string) in input.withIndex()) {
            if (index == 0) {
                directions = directionR.findAll(string).map { it.value }
            } else if (index > 1) {
                val (value, left, right) = nodeR.matchEntire(string)
                    ?.destructured
                    ?: throw IllegalArgumentException("Incorrect input line")

                nodeList.add(Node(value, left, right))
            }
        }
        var steps: Long = 0
        var currentNodes = nodeList.filter { it.value.last() == 'A' }.toMutableList()
        val listOfPathCount: MutableList<Int> = MutableList(currentNodes.count()) { 0 }
        val listOfCycle: MutableList<NodeCycle> = MutableList(currentNodes.count()) { NodeCycle(0) }
        while (true) {
            for (direction in directions) {
                val copyList: MutableList<Node> = emptyList<Node>().toMutableList()
                for ((index, currentNode) in currentNodes.withIndex()) {
                    """${currentNode.value} : ${currentNode.left}, ${currentNode.right}""".println()
                    var nextNode: Node
                    if (direction == "L") {
                        nextNode = nodeList.first { it.value == currentNode.left }
                    } else {
                        nextNode = nodeList.first { it.value == currentNode.right }
                    }
                    "Next node: ${nextNode.value}: ${nextNode.left}, ${nextNode.right}".println()
                    steps++
                    if (nextNode.value.last() == 'Z' && !listOfCycle[index].isComplete()) {
                        listOfCycle[index].occurence(steps)
                    }
                    copyList.add(nextNode)
                }

                currentNodes = copyList

                if (listOfCycle.all { it.isComplete() }) {
                    listOfCycle.forEach { it.cycleLength().println() }
                    return listOfCycle.lcm()
                }
            }
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08/Day08_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day08/Day08")
 //   part1(testInput).println()
  //  part2(testInput).println()
  //  part1(input).println()
    part2(input).println() // 144214639509102 // 72107319754551
}
