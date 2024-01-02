package day15

import println
import readInput

typealias Step = String

fun Step.hash(): Int {
    var sum = 0
    for (char in this) {
        sum += char.code
        sum *= 17
        sum %= 256
    }
    return sum
}

fun main() {
    fun part1(input: List<String>): Int {
        val steps: List<Step> = input.first().split(",")
        return steps.sumOf { it.hash() }
    }

    fun part2(input: List<String>): Int {
        val steps: List<Step> = input.first().split(",")
        val boxes = List<MutableMap<String, Int>>(256) { mutableMapOf() }
        for (step in steps) {
            val split = step.split("=")
            if (split.count() > 1) {
                val box = split[0].hash()
                val label = split[0]
                val focalLength = split[1].toInt()
                if (boxes[box].contains(label)) {
                    boxes[box].replace(label, focalLength)
                } else {
                    boxes[box].put(label, focalLength)
                }
            } else {
                val label = step.dropLast(1)
                val box = label.hash()
                boxes[box].remove(label)
            }
        }
        var sum = 0
        for ((boxIndex, box) in boxes.withIndex()) {
            for ((placeInBox, lens) in box.toList().withIndex()) {
                sum += (boxIndex + 1) * (placeInBox + 1) * lens.second
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15/Day15_test")
    check(part1(testInput) == 1320)

    val input = readInput("Day15/Day15")
   // part1(testInput).println()
   // part1(input).println()
   // part2(testInput).println()
    part2(input).println()
}
