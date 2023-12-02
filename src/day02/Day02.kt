package day02

import println
import readInput
import kotlin.math.max

enum class BallColor(val lowercase: String) {
    RED("red"),
    GREEN("green"),
    BLUE("blue")
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
fun Sequence<SetOfBall>.isGameValid(red: Int, green: Int, blue: Int) : Boolean {
    // return filter { it.color == BallColor.BLUE }.reduce {acc, ball -> acc.ballCount + ball.ballCount} <= blue
    var redCount: Int = 0
    var greenCount: Int = 0
    var blueCount: Int = 0

    for (ball in this) {
        when(ball.color) {
            BallColor.RED -> redCount += ball.ballCount
            BallColor.GREEN -> greenCount += ball.ballCount
            BallColor.BLUE -> blueCount += ball.ballCount
        }
        if (redCount > red || greenCount > green || blueCount > blue) {
         //   redCount.println()
         //   greenCount.println()
         //   blueCount.println()

            return false
        }
    }
   // redCount.println()
   // greenCount.println()
   // blueCount.println()
    return true
}

fun Sequence<SetOfBall>.minimumSet() : Triple<SetOfBall, SetOfBall, SetOfBall> {
    var redCount: Int = 0
    var greenCount: Int = 0
    var blueCount: Int = 0

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

public inline fun <T> Iterable<T>.multiplicationOf(selector: (T) -> Int): Int {
    var multiplication: Int = 1.toInt()
    for (element in this) {
        multiplication *= selector(element)
    }
    return multiplication
}
fun Triple<SetOfBall, SetOfBall, SetOfBall>.power(): Int {
    return this.toList().multiplicationOf { it.ballCount }
}
fun main() {
    val gameR = """Game (\d+): (.*)""".toRegex()
    val setsOfBallR = """(\d+) (red|green|blue)""".toRegex()

    fun part1(input: List<String>): Int {
        var idsSum = 0
        for (string in input) {
            val (gameID, games) = gameR
                .matchEntire(string)
                ?.destructured
                ?: throw IllegalArgumentException("Incorrect input line")
           // gameID.println()
           // games.println()
            // setsOfBallR.findAll(games).forEach { it.value.println() }

            val setOfBall = setsOfBallR.findAll(games).map {
                val splitted = it.value.split(" ")
                val ballColor = BallColor.valueOf(splitted.last().uppercase())
                SetOfBall(splitted.first().toInt(), color = ballColor)
            }


            // games.println()
            // setOfBall.isGameValid(12, 13, 14).println()
            if (setOfBall.isValid(12, 13, 14)) {
                gameID.toInt().println()
                idsSum += gameID.toInt()
            }
        }
        return idsSum
    }

    fun part2(input: List<String>): Int {
        var powerSum = 0
        for (string in input) {
            val (gameID, games) = gameR
                .matchEntire(string)
                ?.destructured
                ?: throw IllegalArgumentException("Incorrect input line")
            // gameID.println()
            // games.println()
            // setsOfBallR.findAll(games).forEach { it.value.println() }

            val setOfBall = setsOfBallR.findAll(games).map {
                val splitted = it.value.split(" ")
                val ballColor = BallColor.valueOf(splitted.last().uppercase())
                SetOfBall(splitted.first().toInt(), color = ballColor)
            }
            powerSum += setOfBall.minimumSet().power()
        }
        return powerSum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02/Day02_test")

    val input = readInput("Day02/Day02")
 //   part1(testInput).println()
 //   part2(testInput).println()
  //  part1(input).println()
    part2(input).println()
}
