package day01

import println
import readInput
import java.lang.Exception
enum class Digits(val lowercase: String) {
    ONE("one"),
    TWO("two"),
    THREE("three"),
    FOUR("four"),
    FIVE("five"),
    SIX("six"),
    SEVEN("seven"),
    EIGHT("eight"),
    NINE("nine")
}
fun Digits.toInt(): Int {
    return when(this) {
        Digits.ONE -> 1
        Digits.TWO -> 2
        Digits.THREE -> 3
        Digits.FOUR -> 4
        Digits.FIVE -> 5
        Digits.SIX -> 6
        Digits.SEVEN -> 7
        Digits.EIGHT -> 8
        Digits.NINE -> 9
    }
}

typealias CalibrationValue = String
fun CalibrationValue.normalized(): Int {
    return try {
        this.toInt()
    } catch(e: Exception) {
        Digits.valueOf(this.uppercase()).toInt()
    }
}
fun String.calibrationValues(): Pair<CalibrationValue, CalibrationValue> {
    val stringDigits = Digits.entries.joinToString(separator = "|") { it.lowercase }
    val stringDigitsReverse = Digits.entries.joinToString(separator = "|") { it.lowercase.reversed() }
    val regex = """(\d|$stringDigits)""".toRegex()
    val regexReverse = """(\d|$stringDigitsReverse)""".toRegex()
    return Pair(
        first = regex.find(this)!!.value,
        second = regexReverse.find(this.reversed())!!.value
    )
}
fun main() {
    fun part1(input: List<String>): Int {
        var sumCalibrationValues = 0
        for (string in input) {
            val digits = string.filter { it.isDigit() }
            val calibrationValueString = "${digits.first()}${digits.last()}"
            sumCalibrationValues += calibrationValueString.toInt()
        }
        return sumCalibrationValues
    }

    fun part2(input: List<String>): Int {
        var sumCalibrationValues = 0
        for (string in input) {
            val (first, last) = string.calibrationValues()
            val calibrationValueString = "${first.normalized()}${last.reversed().normalized()}"
            // "$calibrationValueString -> $string".println()
            sumCalibrationValues += calibrationValueString.toInt()
        }
        return sumCalibrationValues
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    val input = readInput("day01/Day01")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println() // 54978 // 54985
}
