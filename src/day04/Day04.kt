package day04

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
    val testInput = readInput("Day04/Day04_test")
    check(part1(testInput) == 1)

    val input = readInput("Day04/Day04")
    part1(input).println()
    part2(input).println()
}
