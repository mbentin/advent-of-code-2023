package day12

import println
import readInput

data class Row(val record: String, val damages: List<Int>){
    fun key() {
        val key = record to damages
    }

    fun description(): String {
        return "Record: $record | Damages: $damages"
    }

    fun arrangementCount(): Long {
        return arrangementCount(record, damages)
    }

    private fun arrangementCount(record: String, damages: List<Int>): Long {
        if (record.isEmpty()) {
            return if (damages.isEmpty()) 1 else 0
        }

        return when(record.first()) {
            '?' -> {
                arrangementCount(record.substring(1), damages) +
                        arrangementCount("#${record.substring(1)}", damages)
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
                            else -> arrangementCount(record.drop(thisDamage + 1), remainingDamage)
                        }
                    } else 0
                }
            }
            '.' -> arrangementCount(record.dropWhile { it == '.' }, damages)
            else -> throw IllegalStateException("Not a valid symbol")
        }
    }
}


fun main() {
    val numRegex = """(\d+)""".toRegex()

    fun part1(input: List<String>): Long {
        var sumOfArrangements = 0L
        for(string in input) {
            val infos = string.split(" ")
            val groups = numRegex.findAll(infos[1]).map { it.value.toInt() }.toList()
            val row = Row(infos[0], groups)
            row.description().println()
            val count = row.arrangementCount()
            count.println()
            sumOfArrangements += count
        }
        return sumOfArrangements
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12/Day12_test")
   // check(part1(testInput) == 1)

    val input = readInput("Day12/Day12")
   // part1(testInput).println()
    part1(input).println()
   // part2(input).println()
}
