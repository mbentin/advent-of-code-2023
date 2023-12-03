package day02

import multiplicationOf
import println
import readInput
import kotlin.math.max

enum class BallColor {
    RED,
    GREEN,
    BLUE
}
data class SetOfBall(val ballCount: Int, val color: BallColor)

fun Sequence<SetOfBall>.isValid(red: Int, green: Int, blue: Int) : Boolean {
    var result = true
    for (ball in this) {
       result = when (ball.color) {
            BallColor.RED -> ball.ballCount <= red
            BallColor.GREEN -> ball.ballCount <= green
            BallColor.BLUE -> ball.ballCount <= blue
        }.and(result)
    }
    return result
}
fun Sequence<SetOfBall>.minimumSet() : Triple<SetOfBall, SetOfBall, SetOfBall> {
    var redCount = 0
    var greenCount = 0
    var blueCount = 0

    for (ball in this) {
        when(ball.color) {
            BallColor.RED -> redCount = max(redCount, ball.ballCount)
            BallColor.GREEN -> greenCount = max(greenCount, ball.ballCount)
            BallColor.BLUE -> blueCount = max(blueCount, ball.ballCount)
        }
    }

    return Triple(
        SetOfBall(redCount, BallColor.RED),
        SetOfBall(greenCount, BallColor.GREEN),
        SetOfBall(blueCount, BallColor.BLUE)
    )
}

fun Triple<SetOfBall, SetOfBall, SetOfBall>.power(): Int {
    return this.toList().multiplicationOf { it.ballCount }
}
fun main() {
    val gameR = """Game (\d+): (.*)""".toRegex()
    val setsOfBallR = """(\d+) (red|green|blue)""".toRegex()

    fun parse(input: String) : Pair<Int,Sequence<SetOfBall>> {
        val (gameID, games) = gameR
            .matchEntire(input)
            ?.destructured
            ?: throw IllegalArgumentException("Incorrect input line")

        val setOfBall = setsOfBallR.findAll(games).map {
            val split = it.value.split(" ")
            val ballColor = BallColor.valueOf(split.last().uppercase())
            SetOfBall(split.first().toInt(), color = ballColor)
        }

        return Pair(gameID.toInt(), setOfBall)
    }

    fun part1(input: List<String>): Int {
        var idsSum = 0
        for (string in input) {
            val (gameID, setOfBall) = parse(string)

            if (setOfBall.isValid(12, 13, 14)) {
                idsSum += gameID
            }
        }
        return idsSum
    }

    fun part2(input: List<String>): Int {
        var powerSum = 0
        for (string in input) {
            val (_, setOfBall) = parse(string)
            powerSum += setOfBall.minimumSet().power()
        }
        return powerSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02/Day02_test")

    val input = readInput("Day02/Day02")
    part1(testInput).println()
    part2(testInput).println()
    part1(input).println()
    part2(input).println()
}
