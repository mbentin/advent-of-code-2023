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
    init {
        val length = input.first().count()
        dish = Array(length) { arrayOfNulls(input.size) }
        for ((i, line) in input.withIndex()) {
            for ((j, char) in line.withIndex()) {
                dish[j][i] = Rock.getRockBySymbol(char)
            }
        }
    }

    fun tiltedBoard(): Board {
        val copy = dish.copyOf()
        for ((i, line) in dish.withIndex()) {
            var shift = 0
            for ((j, rock) in line.withIndex()) {
                when(rock) {
                    null -> shift--
                    Rock.Rounded -> {
                        copy[i][j + shift] = rock
                        if (shift < 0)  {
                            copy[i][j] = null
                            // shift++
                        }
                    }
                    Rock.Squared -> {
                        shift = 0
                    }
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
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14/Day14_test")
   // check(part1(testInput) == 1)

    val input = readInput("Day14/Day14")
 //   part1(testInput).println()
    part1(input).println()
 //   part2(input).println()
 //   part2(input).println()
}
