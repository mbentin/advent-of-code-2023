package day03

import multiplicationOf
import println
import readInput

class SchemaLine(val symbols: Sequence<MatchResult>, val numbers: Sequence<MatchResult>)

fun IntRange.isAdjacent(range: IntRange): Boolean {
    val extendedRange = IntRange(range.first - 1, range.last + 1)
    return this.first <= extendedRange.last && this.last >= extendedRange.first
}

fun Sequence<MatchResult>.isAdjacent(range: IntRange): Boolean {
    return this.map { range.isAdjacent(it.range) }
        .fold(false) { acc, bool -> acc || bool }
}

fun main() {
    val numbersR = """(\d+)""".toRegex()

    fun part1(input: List<String>): Int {
        var previousLine: SchemaLine? = null
        var topSymbols: Sequence<MatchResult>? = null
        val symbolsR = """([-+*#@/$&%=])""".toRegex()
        var sumOfPartNumbers = 0

        for ((index, string) in input.withIndex()) {
            val numbersFound = numbersR.findAll(string)
            val symbolsFound = symbolsR.findAll(string)
            var isAdjacentToTop = false

            "NEWLINE".println()

            if (previousLine != null) {
                // Calculate the previous line
                for (number in previousLine.numbers) {
                    if (topSymbols != null) {
                        isAdjacentToTop = topSymbols.isAdjacent(number.range)
                    }
                    val isAdjacentToPrevious = previousLine
                        .symbols
                        .isAdjacent(number.range)
                    val isAdjacentCurrent = symbolsFound.isAdjacent(number.range)
                    "${number.value.toInt()} : $isAdjacentCurrent, $isAdjacentToPrevious".println()
                    if (isAdjacentToTop || isAdjacentToPrevious || isAdjacentCurrent) {
                        sumOfPartNumbers += number.value.toInt()
                    }
                }
                // Calculate the last line
                if (index == string.lastIndex) {
                    for (number in numbersFound) {
                        val isAdjacentToPrevious = previousLine.symbols.isAdjacent(number.range)
                        val isAdjacentCurrent = symbolsFound.isAdjacent(number.range)
                        "${number.value.toInt()} : $isAdjacentCurrent, $isAdjacentToPrevious".println()
                        if (isAdjacentToPrevious || isAdjacentCurrent) {
                            sumOfPartNumbers += number.value.toInt()
                        }
                    }
                }
            }
            if (index >= 2) {
                topSymbols = previousLine?.symbols
            }
            previousLine = SchemaLine(symbolsFound, numbersFound)
        }
        return sumOfPartNumbers
    }

    fun part2(input: List<String>): Int {
        val starR = """([*])""".toRegex()
        var sumOfGearRatios = 0
        var previousLine: SchemaLine? = null
        var topNumbers: Sequence<MatchResult>? = null

        for ((index, string) in input.withIndex()) {
            val numbersFound = numbersR.findAll(string)
            val starFound = starR.findAll(string)
            var adjacentToTop: Sequence<Pair<Boolean, String>> = emptySequence()
            "NEW LINE: $index".println()
            if (previousLine != null) {
                // Calculate the previous line
                for (star in previousLine!!.symbols) {
                    if (topNumbers != null) {
                        adjacentToTop = topNumbers
                            .map { Pair(star.range.isAdjacent(it.range), it.value) }
                    }
                    val adjacentToPrevious = previousLine!!
                        .numbers
                        .map { Pair(star.range.isAdjacent(it.range), it.value) }
                    val adjacentCurrent = numbersFound
                        .map { Pair(star.range.isAdjacent(it.range), it.value) }
                    val adjacentValues = (adjacentToTop + adjacentToPrevious + adjacentCurrent).filter { it.first }
                    if (adjacentValues.count() == 2) {
                        sumOfGearRatios += adjacentValues.multiplicationOf { it.second.toInt() }
                    }
                }
                // Calculate the last line
                if (index == string.lastIndex) {
                    for (star in starFound) {
                        val adjacentToPrevious = previousLine!!
                            .numbers
                            .map { Pair(star.range.isAdjacent(it.range), it.value) }

                        val adjacentCurrent = numbersFound
                            .map { Pair(star.range.isAdjacent(it.range), it.value) }

                        val adjacentsCount = (adjacentToPrevious + adjacentCurrent).filter { it.first }.count()
                        "Adjacent count: $adjacentsCount".println()
                        if (adjacentsCount == 2) {
                            sumOfGearRatios += (adjacentToPrevious + adjacentCurrent).filter { it.first }.multiplicationOf { it.second.toInt() }
                        }
                    }
                }
            }
            if (index >= 1) {
                topNumbers = previousLine?.numbers
            }
            previousLine = SchemaLine(starFound, numbersFound)
        }

        return sumOfGearRatios
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03/Day03_test")

    val input = readInput("Day03/Day03")
  //  part1(testInput).println()
  //  part2(testInput).println()
  //  part1(input).println()
    part2(input).println()
}
