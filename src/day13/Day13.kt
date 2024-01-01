package day13

import println
import readInput
import kotlin.math.absoluteValue

typealias Pattern = List<String>

fun Pattern.transposed(): Pattern {
    val size = this.first().count()

    val transposed = MutableList(size) {
        ""
    }

    for (line in this) {
        for (i in 0 ..< size) {
            val sb = StringBuilder(transposed[i])
            sb.append(line[i])
            transposed[i] = sb.toString()
        }
    }
    return transposed.toList()
}
fun Pattern.description(): String {
    var sb = StringBuilder("")
    for (line in this) {
        sb.append(line)
            .append(": Line")
            .append("\n")
    }
    return sb.toString()
}
fun Pattern.analyseFrom(index: Int): Boolean {
    var up = index + 1
    var currentState = false
    for (i in index downTo 0) {
        if (up >= this.size) {
            return currentState
        }
        if (this[i] != this[up]) {
            return false
        }
        currentState = true
        up++
    }
    return currentState
}
fun Pattern.mirrorIndex(): Int {
    val res = indices.filter { this.analyseFrom(it) }
    if (res.isEmpty()) return 0
    // indices.filter { this.analyseFrom(it) }.println()
    val max = res.max()
    "MAX: $max".println()
    return max + 1
}
fun Pattern.mirrorIndex(filter: Int): Int {
    val res = indices
        .filter { this.analyseFrom(it) }
        .filter { it != filter - 1 }
    if (res.isEmpty()) return 0
    // indices.filter { this.analyseFrom(it) }.println()
    val max = res.first()
    "MAX: $max".println()
    return max + 1
}

fun Pattern.notesSummary(): Int {
    val mirrorIndex = this.mirrorIndex()
    return if (mirrorIndex > 0) {
        100 * mirrorIndex
    } else {
        this.transposed().mirrorIndex()
    }
}
fun Pattern.notesSummaryWithCorrection(): Int {
    val originalMirror = mirrorIndex()
    val originalMirrorTransposed = if (originalMirror == 0) transposed().mirrorIndex() else 0
    for ((index, line) in this.withIndex()) {
        for (i in line.indices) {
            val char = this[index][i]
            val sb = StringBuilder(line)
            val copy = this.toMutableList()
            if (char == '.') {
                sb.setCharAt(i, '#')
            } else if (char == '#') {
                sb.setCharAt(i, '.')
            }

            copy[index] = sb.toString()
            val mirrorIndex = copy.mirrorIndex(filter = originalMirror)
            "Changed line: ${sb} : index: $mirrorIndex".println()
            if (mirrorIndex > 0 && mirrorIndex != originalMirror) {
                "FOUND Index: normal".println()
                copy.description().println()
                return 100 * mirrorIndex
            } else {
                val transposed = copy.transposed()
                val transposedIndex = transposed.mirrorIndex(filter = originalMirrorTransposed)
                "transposedIndex: $transposedIndex".println()
                if (transposedIndex > 0 && transposedIndex != originalMirrorTransposed) {
                    "FOUND Index: transposed".println()
                    transposed.description().println()
                    return transposedIndex
                }
            }
        }
    }
    "NO RETURN. Original: $originalMirror, transposed: $originalMirrorTransposed".println()
    return 0
}

fun main() {
    // val regex = """.+""".toRegex()
    fun part1(input: List<String>): Int {
        var startIndex = 0
        val patterns = mutableListOf<Pattern>()

        for ((index, string) in input.withIndex()) {
            if (string.isEmpty()) {
                // analyze previous pattern
                val pattern: Pattern = input.slice(startIndex..< index)
                patterns.add(pattern)
                startIndex = index + 1
            }
        }

        var sum = 0
        for (pattern in patterns) {
            pattern.description().println()
            pattern.transposed().description().println()
            val note = pattern.notesSummary()
            note.println()
            sum += note
        }

        return sum
    }

    fun part2(input: List<String>): Int {
        var startIndex = 0
        val patterns = mutableListOf<Pattern>()

        for ((index, string) in input.withIndex()) {
            if (string.isEmpty()) {
                // analyze previous pattern
                val pattern: Pattern = input.slice(startIndex..< index)
                patterns.add(pattern)
                startIndex = index + 1
            }
        }

        var sum = 0
        for (pattern in patterns) {
            pattern.description().println()
            pattern.transposed().description().println()
            val note = pattern.notesSummaryWithCorrection()
            note.println()
            sum += note
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13/Day13_test")
   // check(part1(testInput) == 405)

    val input = readInput("Day13/Day13")
   //  part1(testInput).println()
  //  part1(input).println() // 31265
//    part2(testInput).println()
     part2(input).println() // 34125 < // 30255 < // 33980 <
}
