package day06

import multiplicationOf
import println
import readInput

class Race(val time: Long, val distance: Long) {
    fun winnings(): List<Long> {
        var list = mutableListOf<Long>()
        for (i in 1..time) {
            val traveled = (time - i) * i
            if (traveled > distance) {
                list.add(traveled)
            }
            "i:$i time:$time traveled:$traveled distance:$distance Added:${(traveled > distance)}".println()
        }
        return list
    }
}
fun main() {
    val timeR = """(\d+)""".toRegex()
    fun part1(input: List<String>): Int {
        var time: Sequence<Int> = emptySequence()
        var distance: Sequence<Int> = emptySequence()
        for ((index, string) in input.withIndex()) {
            if (index == 0) {
                time = timeR.findAll(string).map { it.value.toInt() }
            } else {
                distance = timeR.findAll(string).map { it.value.toInt() }
            }
        }
        val races = time.zip(distance).map { Race(it.first.toLong(), it.second.toLong()) }

        return races
            .map { it.winnings().count() }
            .onEach(::println)
            .multiplicationOf { it }
    }

    fun part2(input: List<String>): Int {
        val timeR = """(\d+)""".toRegex()
        var time: Long = 0L
        var distance: Long = 0L
        for ((index, string) in input.withIndex()) {
            if (index == 0) {
                time = timeR.findAll(string).map { it.value }.joinToString("").toLong()
            } else {
                distance = timeR.findAll(string).map { it.value }.joinToString("").toLong()
            }
        }

        val race = Race(time, distance).winnings().count()
        return race
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06/Day06_test")
    // check(part1(testInput) == 625968)

    val input = readInput("Day06/Day06")
    // part1(testInput).println()
   // part2(testInput).println()
   //  part1(input).println()
    part2(input).println()
}
