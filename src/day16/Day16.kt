package day16

import Direction
import memoize
import println
import readInput

interface RayDirection {
    fun nextDirection(provenance: Direction): List<Direction>
}
enum class Object: RayDirection {
    Empty {
        override fun nextDirection(provenance: Direction): List<Direction> {
            return when(provenance) {
                Direction.NORTH -> listOf(Direction.NORTH)
                Direction.EAST -> listOf(Direction.EAST)
                Direction.SOUTH -> listOf(Direction.SOUTH)
                Direction.WEST -> listOf(Direction.WEST)
            }
        }
    },
    MirrorLeft {
        override fun nextDirection(provenance: Direction): List<Direction> {
            return when(provenance) {
                Direction.NORTH -> listOf(Direction.EAST)
                Direction.EAST -> listOf(Direction.NORTH)
                Direction.SOUTH -> listOf(Direction.WEST)
                Direction.WEST -> listOf(Direction.SOUTH)
            }
        }
    },
    MirrorRight {
        override fun nextDirection(provenance: Direction): List<Direction> {
            return when(provenance) {
                Direction.NORTH -> listOf(Direction.EAST)
                Direction.EAST -> listOf(Direction.SOUTH)
                Direction.SOUTH -> listOf(Direction.WEST)
                Direction.WEST -> listOf(Direction.NORTH)
            }
        }
    },
    SplitterVertical {
        override fun nextDirection(provenance: Direction): List<Direction> {
            return when(provenance) {
                Direction.NORTH -> listOf(Direction.NORTH)
                Direction.EAST -> listOf(Direction.NORTH, Direction.SOUTH)
                Direction.SOUTH -> listOf(Direction.SOUTH)
                Direction.WEST -> listOf(Direction.NORTH, Direction.SOUTH)
            }
        }
    },
    SplitterHorizontal {
        override fun nextDirection(provenance: Direction): List<Direction> {
            return when(provenance) {
                Direction.NORTH -> listOf(Direction.WEST, Direction.EAST)
                Direction.EAST -> listOf(Direction.EAST)
                Direction.SOUTH -> listOf(Direction.WEST, Direction.EAST)
                Direction.WEST -> listOf(Direction.WEST)
            }
        }
    },
    Energized {
        override fun nextDirection(provenance: Direction): List<Direction> {
            return when(provenance) {
                Direction.NORTH -> listOf(Direction.NORTH)
                Direction.EAST -> listOf(Direction.EAST)
                Direction.SOUTH -> listOf(Direction.SOUTH)
                Direction.WEST -> listOf(Direction.WEST)
            }
        }
    }
    ;

    fun representation(): Char {
        return when(this) {
            Empty -> '.'
            MirrorLeft -> '/'
            MirrorRight -> '\\'
            SplitterVertical -> '|'
            SplitterHorizontal -> '-'
            Energized -> '#'
        }
    }
    
    companion object {
        fun getObject(char: Char): Object {
            return when (char) {
                '.' -> Empty
                '/' -> MirrorLeft
                '\\' -> MirrorRight
                '|' -> SplitterVertical
                '-' -> SplitterHorizontal
                else -> throw IllegalArgumentException("Invalid char")
            }
        }
    }
}

class Position(val x: Int, val y: Int) {
    operator fun plus(position: Position): Position {
        return Position(this.x + position.x, this.y + position.y)
    }

    fun fitsIn(size: Size): Boolean {
        if (this.x >= 0 && this.x < size.x && this.y >= 0 && this.y < size.y) {
            return true
        }
        return false
    }

    fun description(): String {
       return "($x,$y)"
    }
}
fun Direction.translation(): Position {
    return when(this) {
        Direction.NORTH -> Position(0, -1)
        Direction.EAST -> Position(1, 0)
        Direction.SOUTH -> Position(0, 1)
        Direction.WEST -> Position(-1, 0)
    }
}

typealias Grid = MutableList<MutableList<Object>>
class Size(val x: Int, val y: Int)
fun Grid.display() {
    this.forEach {
        it.forEach { print(it.representation()) }
        "".println()
    }
}
class GridHandler(input: List<String>) {
    val grid: Grid = mutableListOf()
    val energizedGrid: Grid = mutableListOf()
    val directionGrid: MutableList<MutableList<MutableList<Pair<Position, Direction>>>> = mutableListOf()
    private val gridSize: Size
    init {
        val xSize = input.first().count()
        for (string in input) {
            grid.add(string.map { Object.getObject(it) }.toMutableList())
            energizedGrid.add(string.map { Object.getObject(it) }.toMutableList())
            directionGrid.add(string.map { emptyList<Pair<Position, Direction>>().toMutableList() }.toMutableList())
        }
        gridSize = Size(xSize, grid.size)
    }
    fun displayGrid() {
        grid.display()
    }

    fun displayEnergizedGrid() {
        energizedGrid.display()
    }

    fun beamsOn() {
        rayWorker(Position(0, 0), Direction.EAST)
    }

    private val rayWorker = { obj: Position, initialDirection: Direction -> ray(obj, initialDirection) } // .memoize()

    fun ray(obj: Position, initialDirection: Direction): List<Position> {
        if (directionGrid[obj.y][obj.x].contains(Pair(obj, initialDirection))) {
            return emptyList()
        }
        directionGrid[obj.y][obj.x].add(Pair(obj, initialDirection))
        energizedGrid[obj.y][obj.x] = Object.Energized
        val directions = grid[obj.y][obj.x].nextDirection(initialDirection)
        for (direction in directions) {
            if (directions.isEmpty()) "EMPTY".println()
            val expectedPosition = obj + direction.translation()
            // "Position: ${obj.description()} | initialDirection: $initialDirection | ExpectedPosition: ${expectedPosition.description()} | $directions".println()
            if (expectedPosition.fitsIn(gridSize)) {
                rayWorker(expectedPosition, direction)
            }
        }
        return emptyList()
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        val grid = GridHandler(input)
        grid.displayGrid()
        println()
        grid.beamsOn()
        grid.displayEnergizedGrid()
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16/Day16_test")
  //  check(part1(testInput) == 1)

    val input = readInput("Day16/Day16")
    part1(testInput).println()
  //   part1(testInput).println()
  //   part2(input).println()
  //   part2(input).println()
}
