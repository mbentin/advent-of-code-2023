package day17

import Direction
import Grid
import Position
import org.w3c.dom.css.Counter
import println
import readInput
import java.util.Collections
import java.util.PriorityQueue
import kotlin.math.abs

typealias WeightedPosition = Pair<DirectedPosition, Int>
class WeightedPositionComparator {
    companion object : Comparator<WeightedPosition> {
        override fun compare(o1: Pair<DirectedPosition, Int>, o2: Pair<DirectedPosition, Int>): Int {
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
        Path,
        Provenance
    }
    fun display(option: DisplayOption = DisplayOption.Path) {
        for (i in 0..< grid.size().x) {
            for (j in grid.grid[i].indices) {
                val position = Position(j, i)
                when (position) {
                    Position(0, 0) -> print('S')
                    Position(grid.size().x - 1, grid.size().y - 1) -> print('D')
                    else -> {
                        when(option) {
                            DisplayOption.None -> print(grid.get(position))
                            DisplayOption.Path -> if (position in path) print("@") else print(grid.get(position))
                            DisplayOption.Provenance -> {
                                val pos = cameFrom[position]
                                when( if ( pos != null) position.direction(pos) else null) {
                                    Direction.EAST -> print('→')
                                    Direction.SOUTH -> print('↓')
                                    Direction.WEST -> print('←')
                                    Direction.NORTH -> print('↑')
                                    else -> print('0')
                                }
                            }
                        }
                    }
                }
            }
            "".println()
        }
    }
}

data class DirectedPosition(val position: Position, val direction: Direction, val count: Int)

// Unblocking? with https://github.com/Mistborn94/advent-of-code-2023/blob/master/src/main/kotlin/day17/Day17.kt
fun Grid<Int>.neighborsFor(position: DirectedPosition): List<DirectedPosition> {
    return buildList {
        if (position.count < 3) {
            add(DirectedPosition(position.position.translated(position.direction), position.direction, position.count + 1))
        }
        position.direction.sides().forEach {
            add(DirectedPosition(
                position.position.translated(it),
                it,
                1
            ))
        }
    }
        .onEach { it.println() }
        .filter { it.position.fitsIn(this.size()) }
}
fun Grid<Int>.neighborsFor(path: List<Position>, position: Position): List<Position> {
    return neighborsFor(position)
        .onEach { it.println() }
        //.forEach { it.println() }
        // .an
        .filter { it !in path }
        .filter { _ ->
            if (path.count() < 3) return@filter true

            var prec = position
            val directions: MutableList<Direction> = mutableListOf()
            for (pos in path) {
                directions.add(prec.direction(pos))
                prec = pos
            }
            directions.forEach {
                it.println()
            }

            return@filter !directions.all { directions.first() == it }
        }
        .onEach { it.println() }
}
class ElvesMap(input: List<String>) {
    val grid: Grid<Int> = Grid(mutableListOf())

    init {
        for (string in input) {
            grid.add(string.map { it.digitToInt() }.toMutableList())
        }
    }

    fun lastThreePosition(position: Position, cameFrom: MutableMap<Position, Position>): List<Position> {
        if (cameFrom.count() < 3) return emptyList()
        var position = position
        val lastThree: MutableList<Position> = mutableListOf()
        for (i in 0.. 2) {
            val prec = cameFrom[position]
            if (prec != null) {
                lastThree.add(prec)
                position = prec
            }
        }
        return lastThree.toList()
    }
    fun heuristic(pos1: Position, pos2: Position): Int {
        return abs(pos1.x - pos2.x) + abs(pos1.y - pos2.y)
    }
    fun shortestPath(grid: Grid<Int>): SearchResult<Int> {
        // https://www.redblobgames.com/pathfinding/a-star/implementation.html
        val start = DirectedPosition(Position(0, 0), Direction.EAST, 0)
        val destination = Position(grid.size().x - 1, grid.size().y - 1)
        val cameFrom: MutableMap<Position, Position> = mutableMapOf()
        val costSoFar: MutableMap<Position, Int> = mutableMapOf()

        val frontier: PriorityQueue<Pair<DirectedPosition, Int>> = PriorityQueue(WeightedPositionComparator)

        frontier.add(Pair(start, grid.get(start.position) + heuristic(start.position, destination)))
        cameFrom[start.position] = start.position
        costSoFar[start.position] = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.poll().first

            if (current.position == destination) {
                break
            }
           // val lastThree = lastThreePosition(current, cameFrom)
            val neighbors = grid.neighborsFor(current)
            "New frontier".println()
            for (next in neighbors) {
                next.println()
                val newCost = costSoFar[current.position]?.plus(grid.get(next.position))
                next.position.description().println()
                "Cost so far: ${costSoFar[current.position]}, Grid Next: ${grid.get(next.position)}".println()
                newCost.println()
                if (newCost != null) {
                    if (next.position !in costSoFar  ||  newCost < (costSoFar[next.position] ?: 0)) {
                        costSoFar[next.position] = newCost
                        cameFrom[next.position] = current.position
                        val priority = newCost + heuristic(next.position, destination)
                        frontier.add(Pair(next, priority))
                    }
                }
            }
        }
        return SearchResult(grid, start.position, destination, cameFrom, costSoFar)
    }

    fun heat(): Int {
        val search = shortestPath(grid)
        search.display()
        "".println()
        search.display(SearchResult.DisplayOption.Provenance)
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
   // part1(input).println()
   // part2(testInput).println()
   // part2(input).println()
}
