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

    for ((index, line) in this.withIndex()) {
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

fun Pattern.shouldBeTransposed(): Boolean {
    val values = this.withIndex()
        .groupBy({ it.value }, {it.index})
        .filterValues { it.count() > 1 }

    val valuesTransposed = this.transposed().withIndex()
        .groupBy({ it.value }, {it.index})
        .filterValues { it.count() > 1 }
    values.count().println()
    valuesTransposed.count().println()
    return values.count() <= valuesTransposed.count()
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
  //  fun Pattern.middle(): Int? {
  //      var previous = ""
//
  //      for ((index, line) in this.withIndex()) {
  //          if (line == previous) {
  //              return index - 1
  //          }
  //          previous = line
  //      }
  //      return null
  //  }

 //   val middle = middle() ?: return 0

  //  middle.let {
  //      var up = it + 1
  //      for (i in it downTo 0) {
  //          if (up >= this.size ) {
  //              break
  //          }
  //          if (this[i] != this[up]) {
  //              return 0
  //          }
  //          up++
  //      }
  //  }

    val res = indices.filter { this.analyseFrom(it) }
    if (res.isEmpty()) return 0
    indices.filter { this.analyseFrom(it) }.println()
    val max = res.max()
    "MAX: $max".println()
    return max + 1 // middle + 1
}

fun Pattern.notesSummary(): Int {
    val mirrorIndex = this.mirrorIndex()
    return if (mirrorIndex > 0) {
        100 * mirrorIndex
    } else {
        this.transposed().mirrorIndex()
    }
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
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13/Day13_test")
   // check(part1(testInput) == 405)

    val input = readInput("Day13/Day13")
   //  part1(testInput).println()
    part1(input).println() // 31265
    // part2(input).println()
}
