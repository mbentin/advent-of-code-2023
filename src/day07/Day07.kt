package day07

import println
import readInput

enum class Strength {
    A, K, Q, J, T, N, H, P, S, C, V, D, Z;

    companion object  {
        fun map(string: Char): Strength {
            return when(string) {
                'A' -> A
                'K' -> K
                'Q' -> Q
                'J' -> J
                'T' -> T
                '9' -> N
                '8' -> H
                '7' -> P
                '6' -> S
                '5' -> C
                '4' -> V
                '3' -> D
                '2' -> Z
                else -> {
                    throw error("Not found")
                }
            }
        }
    }
}

enum class Type {
    FIVEOF, FOUROF, FULL, THREEOF, TWOPAIR, ONEPAIR, HIGHCARD
}
class Hand(val cards: String, val bid: Int) {

    fun strength(): List<Strength> {
       return cards
           .map { Strength.map(it) }
           // .also { it.println() }
    }

    companion object {
        fun comparator(): Comparator<Hand> {
            return compareByDescending<Hand>{ it.classification() }
                .thenByDescending { it.strength()[0] }
                .thenByDescending { it.strength()[1] }
                .thenByDescending { it.strength()[2] }
                .thenByDescending { it.strength()[3] }
                .thenByDescending { it.strength()[4] }
        }
    }
    fun classification(): Type {
        val occurrencesMap = mutableMapOf<Char, Int>()
        for (card in cards) {
            occurrencesMap.putIfAbsent(card, 0)
            occurrencesMap[card] = occurrencesMap[card]!! + 1
        }
        val types = occurrencesMap
            .toList()
            .sortedByDescending { (_, value) -> value }
            // .onEach { log("${it.first}, ${it.second}") }

        for ((index, type) in types.withIndex()) {
            return when(type.second) {
                5 -> Type.FIVEOF
                4 -> Type.FOUROF
                3 -> {
                    if (types[index + 1].second == 2) {
                        return Type.FULL
                    }
                    Type.THREEOF
                }
                2 -> {
                    if (types[index + 1].second == 2) {
                        return Type.TWOPAIR
                    }
                    "ONEPAIR with $cards. ${types[index + 1].second}".println()
                    Type.ONEPAIR
                }
                1 -> Type.HIGHCARD
                else -> { Type.HIGHCARD }
            }
        }
        return Type.HIGHCARD
    }
}

fun log(message: String) {
    message.println()
}
fun main() {
    val handR = """(.*) (\d+)""".toRegex()
    fun part1(input: List<String>): Int {
        val hands = mutableListOf<Hand>()
        for (string in input) {
             val (hand, bid) = handR.matchEntire(string)
                 ?.destructured
                 ?: throw IllegalArgumentException("Incorrect input line")
            hands.add(Hand(hand, bid.toInt()))
        }
       val res = hands
           // .onEach { log("CARDS: ${it.cards}, class: ${it.classification()} BID: ${it.bid}") }
           .sortedWith (Hand.comparator())
           // .onEach { log("CARDS: ${it.cards}, classification: ${it.classification()} BID: ${it.bid}") }
           .withIndex()
          // .onEach { log("Index: ${it.index}, BID: ${it.value.bid}") }
           .sumOf { (it.index + 1) * it.value.bid }
           // .also { it.println() }

        return res
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07/Day07_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day07/Day07")
    // part1(testInput).println()
     part1(input).println() // 246003757
    // part2(testInput).println()
}