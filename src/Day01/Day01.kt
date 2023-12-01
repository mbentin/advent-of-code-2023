import java.lang.Exception

enum class Digits {one, two, three, four, five, six, seven, eight, nine}
typealias CalibrationValue = String
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

    fun Digits.toInt(): Int {
       return when(this) {
            Digits.one -> 1
            Digits.two -> 2
            Digits.three -> 3
            Digits.four -> 4
            Digits.five -> 5
            Digits.six -> 6
            Digits.seven -> 7
            Digits.eight -> 8
            Digits.nine -> 9
        }
    }

    fun String.calibrationValues(): Pair<CalibrationValue, CalibrationValue> {
        val stringDigits = Digits.entries.map { it.name }.joinToString(separator = "|")
        val stringDigitsReverse = Digits.entries.map { it.name.reversed() }.joinToString(separator = "|")
        val regex = """(\d|$stringDigits)""".toRegex()
        val regexReverse = """(\d|$stringDigitsReverse)""".toRegex()
        return Pair(
            first = regex.find(this)!!.value,
            second = regexReverse.find(this.reversed())!!.value
        )
    }

    fun CalibrationValue.normalized(): Int {
        return try {
            this.toInt()
        } catch(e: Exception) {
            Digits.valueOf(this).toInt()
        }
    }

    fun part2(input: List<String>): Int {
        var sumCalibrationValues = 0
        for (string in input) {
            val (first, last) = string.calibrationValues()
            val calibrationValueString = "${first.normalized()}${last.reversed().normalized()}"
            "$calibrationValueString -> $string".println()
            sumCalibrationValues += calibrationValueString.toInt()
        }
        return sumCalibrationValues
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01/Day01_test")
    val input = readInput("Day01/Day01")
    part1(input).println()
    part2(input).println() // 54978 // 54985
}
