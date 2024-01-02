package day15

import println
import readInput

typealias Step = String

fun Step.hash(): Int {
    var sum = 0
    for (char in this) {
        sum += char.code
        sum *= 17
        sum %= 256
    }
    return sum
}

fun main() {
    fun part1(input: List<String>): Int {
        val steps: List<Step> = input.first().split(",")
        return steps.sumOf { it.hash() }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15/Day15_test")
    check(part1(testInput) == 1320)

    val input = readInput("Day15/Day15")
   // part1(testInput).println()
    part1(input).println()
   // part2(input).println()
   // part2(input).println()
}
