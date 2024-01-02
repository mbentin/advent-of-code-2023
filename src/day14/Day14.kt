package day14

import println
import readInput

enum class Rock {
    Rounded,
    Squared;

    fun representation(): Char {
        return when(this) {
            Rounded -> 'O'
            Squared -> '#'
        }
    }
    companion object {
        fun getRockBySymbol(symbol: Char): Rock? {
            return when(symbol) {
                '#' -> Squared
                'O' -> Rounded
                else -> null
            }
        }
    }
}

typealias Board = Array<Array<Rock?>>

fun Board.description(): String {
    val stringBuilder = StringBuilder()
    for ((i, line) in this.withIndex()) {
        for ((j, char) in line.withIndex()) {
            stringBuilder.append(if (this[j][i] != null) this[j][i]?.representation() else '.')
        }
        stringBuilder.append("\n")
    }
    return stringBuilder.toString()
}
fun Board.display() {
    for ((i, line) in this.withIndex()) {
        for ((j, char) in line.withIndex()) {
            print( if (this[j][i] != null) this[j][i]?.representation() else '.')
        }
        "".println()
    }
}

fun Board.weight(): Int {
    var coef = this.size
    var weight = 0
    for ((i, line) in this.withIndex()) {
        for ((j, char) in line.withIndex()) {
            if (this[j][i] == Rock.Rounded)  {
                weight += coef
            }
        }
        coef--
    }
    return weight
}
class Platform(input: List<String>) {
    val dish: Board
    val height: Int
    val width: Int
    init {
        val length = input.first().count()
        dish = Array(length) { arrayOfNulls(input.size) }
        for ((i, line) in input.withIndex()) {
            for ((j, char) in line.withIndex()) {
                dish[j][i] = Rock.getRockBySymbol(char)
            }
        }
        height = length
        width = input.size
    }

    fun cycle(): Board {
        tiltedBoard()
        westTiltedBoard()
        southTiltedBoard()
        eastTiltedBoard()
        return dish
    }

    fun runCycles(): Int {
        val boards = mutableMapOf<String, Int>()
        val total = 1_000_000_000

        repeat(total) { iteration ->
            val board = this.cycle()
            val description = board.description()

            if (description in boards) {
                val index = iteration - boards.getValue(description)
                val remainingCycles = (total - iteration) % index
                repeat(remainingCycles - 1) { this.cycle() }
                return dish.weight()
            }

            boards.put(description, iteration)
        }
        return dish.weight()
    }

    fun tiltedBoard(): Board {
        val copy = dish // .copyOf()
        for ((i, line) in dish.withIndex()) {
            var shift = 0
            for ((j, rock) in line.withIndex()) {
                when(rock) {
                    null -> shift--
                    Rock.Rounded -> {
                        copy[i][j + shift] = rock
                        if (shift < 0) copy[i][j] = null
                    }
                    Rock.Squared -> shift = 0
                }
            }
        }
        return copy
    }
    fun westTiltedBoard(): Board {
        val copy = dish // .copyOf()
        for (i in 0 ..< width) {
            var shift = 0
            for (j in 0 ..< height) {
                val rock = copy[j][i]
                when(rock) {
                    null -> shift--
                    Rock.Rounded -> {
                        copy[j + shift][i] = rock
                        if (shift < 0) copy[j][i] = null
                    }
                    Rock.Squared -> shift = 0
                }
            }
        }
        return copy
    }
    fun eastTiltedBoard(): Board {
        val copy = dish // .copyOf()
        for (i in width - 1 downTo 0) {
            var shift = 0
            for (j in height - 1 downTo 0) {
                val rock = copy[j][i]
                when(rock) {
                    null -> shift++
                    Rock.Rounded -> {
                        copy[j + shift][i] = rock
                        if (shift > 0) copy[j][i] = null
                    }
                    Rock.Squared -> shift = 0
                }
            }
        }
        return copy
    }
    fun southTiltedBoard(): Board {
        val copy = dish //.copyOf()
        for (i in height - 1 downTo 0) {
            var shift = 0
            for (j in width - 1 downTo 0) {
                val rock = copy[i][j]
                when(rock) {
                    null -> shift++
                    Rock.Rounded -> {
                        copy[i][j + shift] = rock
                        if (shift > 0) copy[i][j] = null
                    }
                    Rock.Squared -> shift = 0
                }
            }
        }
        return copy
    }

}
fun main() {
    fun part1(input: List<String>): Int {
        val platform = Platform(input)
        platform.dish.display()
        println()
        platform.tiltedBoard().display()
        return platform.tiltedBoard().weight()
    }

    fun part2(input: List<String>): Int {
        val platform = Platform(input)
        platform.dish.display()
        println()
        return platform.runCycles()
    }

    val testInput = readInput("Day14/Day14_test")
    val input = readInput("Day14/Day14")
 //   part1(testInput).println()
 //    part1(input).println()
  //   part2(testInput).println()
     part2(input).println() // 94622 > // 94291 > // 94037 > // 92346 // 93742 OK
}
