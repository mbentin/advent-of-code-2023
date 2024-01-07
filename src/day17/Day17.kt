package day17

import Grid
import Position
import println
import readInput
import java.util.Collections
import java.util.PriorityQueue

typealias WeightedPosition = Pair<Position, Int>

class WeightedPositionComparator {
    companion object : Comparator<WeightedPosition> {
        override fun compare(o1: Pair<Position, Int>, o2: Pair<Position, Int>): Int {
            return o1.second - o2.second
        }
    }
}

class SearchResult<T>(
    val grid: Grid<T>,
    val start: Position,
    val destination: Position,
    val cameFrom: MutableMap<Position, Position>,
    val costSoFar: MutableMap<Position, T>
) {
    val path: List<Position>
    init {
        path = reconstructPath()
    }
    fun reconstructPath(): List<Position> {
        val path = mutableListOf<Position>()
        var current = destination
        if (current !in cameFrom) {
            return path.toList()
        }

        while (current != start) {
            path.add(current)
            current = cameFrom[current]!!
        }

        Collections.swap(path, 0, path.size -1)

        return path.toList()
    }

    enum class DisplayOption {
        None,
        Path
    }
    fun display(option: DisplayOption = DisplayOption.Path) {
        for (i in 0..< grid.size().x) {
            for (j in grid.grid[i].indices) {
                val position = Position(i, j)
                when (position) {
                    Position(0, 0) -> print('S')
                    Position(grid.size().x - 1, grid.size().y - 1) -> print('D')
                    else -> {
                        when(option) {
                            DisplayOption.None -> print(grid.get(position))
                            DisplayOption.Path -> if (position in path) print("@") else print(grid.get(position))
                        }
                    }
                }
            }
            "".println()
        }
    }
}
class ElvesMap(input: List<String>) {
    val grid: Grid<Int> = Grid(mutableListOf())

    init {
        for (string in input) {
            grid.add(string.map { it.digitToInt() }.toMutableList())
        }
    }
    fun shortestPath(grid: Grid<Int>): SearchResult<Int> {
        // https://www.redblobgames.com/pathfinding/a-star/implementation.html
        val start = Position(0, 0)
        val destination = Position(grid.size().x - 1, grid.size().y - 1)
        val cameFrom: MutableMap<Position, Position> = mutableMapOf()
        val costSoFar: MutableMap<Position, Int> = mutableMapOf()

        val frontier: PriorityQueue<Pair<Position, Int>> = PriorityQueue(WeightedPositionComparator)

        frontier.add(Pair(start, grid.get(start)))
        cameFrom[start] = start
        costSoFar[start] = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.poll().first

            if (current == destination) {
                break
            }

            for (next in grid.neighborsFor(current)) {
                val newCost = costSoFar[current]?.plus(grid.get(next))
                if (newCost != null) {
                    if (next !in costSoFar || newCost < (costSoFar[next] ?: 0)) {
                        costSoFar[next] = newCost
                        cameFrom[next] = current
                        frontier.add(Pair(next, newCost))
                    }
                }
            }
        }
        return SearchResult(grid, start, destination, cameFrom, costSoFar)
    }

    fun heat(): Int {
        val search = shortestPath(grid)
        search.display()
        return search.path.sumOf { grid.get(it) }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val map = ElvesMap(input)
        return map.heat()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day17/Day17_test")
    // check(part1(testInput) == 1)

    val input = readInput("Day17/Day17")
    "Results".println()
    part1(testInput).println()
    part1(input).println()
    part2(testInput).println()
    part2(input).println()
}
