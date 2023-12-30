package day12

import memoize
import println
import readInput

class Row {
    val record: String
    val damages: List<Int>

    private constructor(record: String, damages: List<Int>) {
        this.record = record
        this.damages = damages
    }
    constructor(
        string: String
    ) {
        val numRegex = """(\d+)""".toRegex()
        val infos = string.split(" ")
        val groups = numRegex.findAll(infos[1]).map { it.value.toInt() }.toList()
        this.record = infos[0]
        this.damages = groups
    }
    fun key() {
        val key = record to damages
    }

    fun unfolded(): Row {
        val newRecord = (0..4).joinToString("?") { record }
        val newDamages = (0..4).flatMap { damages }
        return Row(newRecord, newDamages)
    }

    fun description(): String {
        return "Record: $record | Damages: $damages"
    }

    fun arrangementCount(): Long {
        return memoizedArrangementCount(record, damages)
    }

    val memoizedArrangementCount = { record: String, damages: List<Int> -> arrangementCount(record, damages) }.memoize()

    private fun arrangementCount(record: String, damages: List<Int>): Long {
        if (record.isEmpty()) {
            return if (damages.isEmpty()) 1 else 0
        }

        return when(record.first()) {
            '?' -> {
                memoizedArrangementCount(record.substring(1), damages) +
                        memoizedArrangementCount("#${record.substring(1)}", damages)
            }
            '#' -> when {
                damages.isEmpty() -> 0
                else -> {
                    val thisDamage = damages.first()
                    val remainingDamage = damages.drop(1)
                    if (thisDamage <= record.length && record.take(thisDamage).none { it == '.' }) {
                        when {
                            thisDamage == record.length -> if (remainingDamage.isEmpty()) 1 else 0
                            record[thisDamage] == '#' -> 0
                            else -> memoizedArrangementCount(record.drop(thisDamage + 1), remainingDamage)
                        }
                    } else 0
                }
            }
            '.' -> memoizedArrangementCount(record.dropWhile { it == '.' }, damages)
            else -> throw IllegalStateException("Not a valid symbol")
        }
    }
}


fun main() {
    fun part1(input: List<String>): Long {
        var sumOfArrangements = 0L
        for(string in input) {
            val row = Row(string)
            row.description().println()
            val count = row.arrangementCount()
            count.println()
            sumOfArrangements += count
        }
        return sumOfArrangements
    }

    fun part2(input: List<String>): Long {
        var sumOfArrangements = 0L
        for(string in input) {
            val row = Row(string)
            // row.description().println()
            val count = row.unfolded().arrangementCount()
           // count.println()
            sumOfArrangements += count
        }
        return sumOfArrangements
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12/Day12_test")
   // check(part1(testInput) == 1)

    val input = readInput("Day12/Day12")
   // part1(testInput).println()
  //  part1(input).println()
   // part2(testInput).println()
    part2(input).println()
}
