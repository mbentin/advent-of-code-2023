package day21

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
    val testInput = readInput("Day21/Day_test")
    check(part1(testInput) == 1)

    val input = readInput("Day21/Day21")
    part1(input).println()
    part2(input).println()
}
