package day05

import println
import readInput
import kotlin.math.min


class Seed(val id: Long)
class Map(val destination: Long, val source: Long, val range: Long) {
    fun offset(): Long {
        return destination - source
    }

    fun sourceRange(): LongRange {
        return LongRange(source, source + range - 1)
    }

    fun mapping(source: Long): Long {
        "Mapping $source ${sourceRange()} ${offset()}".println()
        if (sourceRange().contains(source)) {
            "Mapping contained: ${source + offset()}".println()
            return source + offset()
        }
        "Mapping Source".println()
        return source
    }
}

fun MutableList<Map>.mapping(source: Long): Long {
    for (map in this) {
        val mapping = map.mapping(source)
        if (mapping != source) {
            return mapping
        }
    }
    return source
}
fun main() {
    val seedR = """(\d+)""".toRegex()
    val mapR = """(\d+) (\d+) (\d+)""".toRegex()
    val newCategoryR = """.* map:""".toRegex()

    fun parseMap(input: String): Map? {
        return mapR
                .matchEntire(input)
                ?.destructured
                ?.let {(destination, source, range) ->
                    Map(destination.toLong(), source.toLong(), range.toLong())
                }
    }

    fun isStartingNewCategory(input: String): Boolean {
        newCategoryR.find(input)?.let { return true }
        return false
    }
    fun part1(input: List<String>): Long {
        var seeds: Sequence<Seed> = emptySequence()
        var lowestLocation = Long.MAX_VALUE
        var maps: MutableList<MutableList<Map>> = mutableListOf()
        var currentList: MutableList<Map>? = mutableListOf()
        for ((index, string) in input.withIndex()) {
            if (index == 0) {
                seeds = seedR.findAll(string).map { Seed(it.value.toLong()) }
            } else {
                if (isStartingNewCategory(string)) {
                    maps.addLast(currentList)
                    currentList = mutableListOf()
                    continue
                }
                val map = parseMap(string)
                if (map != null) {
                    currentList?.addLast(map)
                }
            }
        }
        maps.addLast(currentList)

        for (seed in seeds) {
            var localLowest = seed.id
            for (map in maps) {
                localLowest = map.mapping(localLowest)
            }
            "MIN: low: $lowestLocation local: $localLowest Res: ${min(lowestLocation, localLowest)}".println()
            lowestLocation = min(lowestLocation, localLowest)
        }

        return lowestLocation
    }

    fun part2(input: List<String>): Long {
        return input.size.toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05/Day05_test")
    check(part1(testInput) == 35L)

    val input = readInput("Day05/Day05")
    // part1(testInput).prLongln()
    part1(input).println()
    // part2(input).prLongln()
}
