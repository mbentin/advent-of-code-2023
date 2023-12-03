package day03

import println
import readInput

class SchemaLine(val symbols: Sequence<MatchResult>, val numbers: Sequence<MatchResult>)

fun IntRange.isAdjacent(range: IntRange): Boolean {
    val extendedRange = IntRange(range.first - 1, range.last + 1)
    return this.first <= extendedRange.last && this.last >= extendedRange.first
}
fun main() {
    val numbersR = """(\d+)""".toRegex()
    val symbolsR = """([-+*#@/$&%=])""".toRegex()
    var previousLine: SchemaLine? = null
    var topSymbols: Sequence<MatchResult>? = null

    fun part1(input: List<String>): Int {
        var sumOfPartNumbers = 0
        for ((index, string) in input.withIndex()) {
            val numbersFound = numbersR.findAll(string)
            val symbolsFound = symbolsR.findAll(string)
            "NEWLINE".println()
            // numbersFound.forEach { it.value.println() }
            // symbolsFound.forEach { it.value.println() }
            var isAdjacentToTop = false
            if (previousLine != null) {
                // Calculate the previous line
                for (number in previousLine!!.numbers) {
                    if (topSymbols != null) {
                        isAdjacentToTop = topSymbols!!
                            .map { number.range.isAdjacent(it.range) }
                            .fold(false) { acc, bool -> acc || bool }
                    }
                    val isAdjacentToPrevious = previousLine!!
                        .symbols
                        .map { number.range.isAdjacent(it.range) }
                        .fold(false) {acc, bool -> acc || bool}
                    val isAdjacentCurrent = symbolsFound
                        .map { number.range.isAdjacent(it.range) }
                        .fold(false) {acc, bool -> acc || bool}
                    "${number.value.toInt()} : $isAdjacentCurrent, $isAdjacentToPrevious".println()
                    if (isAdjacentToTop || isAdjacentToPrevious || isAdjacentCurrent) {
                        sumOfPartNumbers += number.value.toInt()
                    }
                }
                // Calculate the last line
                if (index == string.lastIndex) {
                    for (number in numbersFound) {
                        val isAdjacentToPrevious = previousLine!!.symbols.map { number.range.isAdjacent(it.range) }.fold(false) {acc, bool -> acc || bool}
                        val isAdjacentCurrent = symbolsFound.map { number.range.isAdjacent(it.range) }.fold(false) {acc, bool -> acc || bool}
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
            index.println()
        }
        return sumOfPartNumbers
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03/Day03_test")

    val input = readInput("Day03/Day03")
  //  part1(testInput).println()
  //  part2(testInput).println()
    part1(input).println()
   // part2(input).println()
}
