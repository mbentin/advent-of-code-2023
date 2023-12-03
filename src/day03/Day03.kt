package day03

import multiplicationOf
import println
import readInput

class SchemaLine(val symbols: Sequence<MatchResult>, val numbers: Sequence<MatchResult>)
class MotorLine(val gears: Sequence<Gear>, val engineParts: Sequence<EnginePart>)
class Gear(private val position: Int, gearSize: Int = 1) {
    constructor(matchResult: MatchResult) : this (
        position = matchResult.range.first
    )

    private val range: IntRange = IntRange(position - gearSize, position + gearSize)

    fun isDriving(range: IntRange): Boolean {
        return this.range.first <= range.last && this.range.last >= range.first
    }
}

data class EnginePart(val value: Int, val range: IntRange) {
    constructor(matchResult: MatchResult) : this (
        matchResult.value.toInt(),
        matchResult.range
    )
}

fun Sequence<EnginePart>.drivenPartsBy(gear: Gear): Sequence<EnginePart> {
    return this.filter { gear.isDriving(it.range) }
}

fun IntRange.isAdjacent(range: IntRange): Boolean {
    val extendedRange = IntRange(range.first - 1, range.last + 1)
    return this.first <= extendedRange.last && this.last >= extendedRange.first
}

fun Sequence<MatchResult>.isAdjacent(range: IntRange): Boolean {
    return this.map { range.isAdjacent(it.range) }
        .fold(false) { acc, bool -> acc || bool }
}

class MotorGearer(
    var gears: Sequence<Gear>,
    private val topLine: Sequence<EnginePart>?,
    private val middleLine: MotorLine,
    private val bottomLine: Sequence<EnginePart>
) {
    private var adjacentToTop: Sequence<EnginePart>? = null
    fun lineGearRatio(): Int {
        var sumOfGearRatios = 0
        for (gear in gears) {
            if (topLine != null) {
                adjacentToTop = topLine.drivenPartsBy(gear)
            }
            val adjacentToPrevious = middleLine
                .engineParts
                .drivenPartsBy(gear)
            val adjacentCurrent = bottomLine.drivenPartsBy(gear)
            val adjacentValues = ((adjacentToTop ?: emptySequence()) + adjacentToPrevious + adjacentCurrent)
            if (adjacentValues.count() == 2) {
                sumOfGearRatios += adjacentValues.multiplicationOf { it.value }
            }
        }
        return sumOfGearRatios
    }
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
        val gearR = """([*])""".toRegex()
        var sumOfGearRatios = 0
        var previousLine: MotorLine? = null
        var topMotorLine: Sequence<EnginePart>? = null

        for ((index, string) in input.withIndex()) {
            val enginePartsFound = numbersR.findAll(string).map { EnginePart(it) }
            val starFound = gearR.findAll(string).map { Gear(it) }
            
            "NEW LINE: $index".println()
            if (previousLine != null) {
                // Calculate the previous line
                val gearer = MotorGearer(
                    previousLine!!.gears,
                    topMotorLine,
                    previousLine!!,
                    enginePartsFound
                )
                sumOfGearRatios += gearer.lineGearRatio()

                // Calculate the last line
               if (index == string.lastIndex) {
                   gearer.gears = starFound
                   sumOfGearRatios += gearer.lineGearRatio()
               }
            }
            if (index >= 1) {
                topMotorLine = previousLine?.engineParts
            }
            previousLine = MotorLine(starFound, enginePartsFound)
        }

        return sumOfGearRatios
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03/Day03_test")

    val input = readInput("Day03/Day03")
    check(part2(input) == 78826761)
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}
