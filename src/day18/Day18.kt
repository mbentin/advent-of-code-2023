package day18

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18/Day18_test")
    check(part1(testInput) == 1)

    val input = readInput("Day18/Day18")
    part1(input).println()
    part2(input).println()
}
