for day_raw in {02..25}; do
    day=$(printf "%02d" "$day_raw")
    mkdir -p "src/day$day"
    touch "src/day$day/Day$day.txt" "src/day$day/Day$day.kt" "src/day$day/Day$(day)_test.txt"
    echo "1" > "src/day$day/Day$(day)_test.txt"
    cat > "src/day$day/Day$day.kt" << EOF
package day$day

import println
import readInput

fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day$day/Day$(day)_test")
    check(part1(testInput) == 1)

    val input = readInput("Day$day/Day$day")
    part1(input).println()
    part2(input).println()
}
EOF
done
