package day04

import println
import readInput
import kotlin.math.pow

class Game(val gameId: Int, val winning: Sequence<Int>, val iHave: Sequence<Int>) {
    fun printNumbers(numbers: Sequence<Int>) {
        for (number in numbers) {
            number.println()
        }
    }

    fun printWinningNumbers() {
        printNumbers(winning)
    }

    fun printIHaveNumbers() {
        printNumbers(iHave)
    }

    fun scratchCardWon(): Int {
        return this.winning.toList().intersect(this.iHave.toList()).count()
    }

    fun points(): Int {
        val count = this.winning.toList().intersect(this.iHave.toList()).count()
        for (itersect in this.winning.toList().intersect(this.iHave.toList())) {
            itersect.println()
        }
        // "GamePoints $gameId: $count".println()
        if (count == 0) {
            return 0
        }
        "GamePoints $gameId: $count, points: ${2.toDouble().pow(count - 1).toInt()}".println()
        return 2.toDouble().pow(count - 1).toInt()
    }
}
fun main() {
    fun parse(input: String): Game {
        val gameR = """Card *(\d+): (.*) \| (.*)""".toRegex()
        val digitR = """(\d+)""".toRegex()
        val (gameID, winning, iHave) = gameR
                .matchEntire(input)
                ?.destructured
                ?: throw IllegalArgumentException("Incorrect input line")
        val winningNumbers = digitR.findAll(winning).map { it.value.toInt() } //  winning.split(" ").map { it.toInt() }
        val iHaveNumbers = digitR.findAll(iHave).map { it.value.toInt() } // iHave.split(" ").map { it.toInt() }
        return Game(gameId = gameID.toInt(), winningNumbers, iHaveNumbers)
    }
    fun part1(input: List<String>): Int {
        var sum = 0
        for (string in input) {
            val game = parse(string)
            sum += game.points()
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var sum = 0
        var map = mutableMapOf<Int, Int>()
        for (i in 1.. input.count() + 1) {
            map[i] = 1
        }
        for ((index, string) in input.withIndex()) {
            val game = parse(string)
            val scratchCarWon = game.scratchCardWon()
            for (j in 1 .. map[game.gameId]!!) {
                for (i in 1..scratchCarWon) {
                    if (game.gameId + i <= input.count() + 1) {
                        map[game.gameId + i] = map[game.gameId + i]!! + 1
                    }
                }
            }
            sum += map[game.gameId]!!
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04/Day04_test")
    // check(part1(testInput) == 13)

    val input = readInput("Day04/Day04")
    // part1(input).println()
    // part1(testInput).println()
    // part2(testInput).println()
    part2(input).println()

}
