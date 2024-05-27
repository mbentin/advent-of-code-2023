package day09

import println
import readInput

typealias NumberSequence = Sequence<Int>

class Numbers(val numbers: Sequence<Int>, val last: Int?) {
    fun nextValue(): Int {
        val toAdd = if (isFinal()) 0 else difference().nextValue()
        return numbers.last() + toAdd
    }

    fun previousValue(): Int {
        val toSubstract = if (isFinal()) 0 else difference().previousValue()
        return numbers.first() - toSubstract
    }
}

fun Numbers.difference(): Numbers {
    var previous = this.numbers.first()
    val list = mutableListOf<Int>()
    for ((index, number) in this.numbers.withIndex()) {
        if (index> 0 ) {
            list.add(number - previous)
            previous = number
        }
    }
    return Numbers(list.asSequence(), list.last())
}

fun Numbers.isFinal(): Boolean {
    return this.numbers.all { it == 0 }
}

fun main() {
    val digitR = """(-?\d+)""".toRegex()
    fun part1(input: List<String>): Int {
        val numbersList= mutableListOf<Numbers>()
        for (string in input) {
            val numbers = digitR.findAll(string).map { it.value.toInt() }
            numbersList.add(Numbers(numbers, numbers.last()))
        }

        return numbersList.sumOf { it.nextValue() }
    }

    fun part2(input: List<String>): Int {
        val numbersList= mutableListOf<Numbers>()
        for (string in input) {
            val numbers = digitR.findAll(string).map { it.value.toInt() }
            numbersList.add(Numbers(numbers, numbers.last()))
        }

        return numbersList.sumOf { it.previousValue() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09/Day09_test")
    check(part1(testInput) == 114)

    val input = readInput("Day09/Day09")
   // part1(testInput).println()
   // part1(input).println()
    part2(testInput).println()
    part2(input).println()

}
