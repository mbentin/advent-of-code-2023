package day16

import Direction
import Position
import Size
import mutableCopyOf
import println
import readInput
import kotlin.math.max

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
                Direction.NORTH -> listOf(Direction.WEST)
                Direction.EAST -> listOf(Direction.SOUTH)
                Direction.SOUTH -> listOf(Direction.EAST)
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

fun Direction.translation(): Position {
    return when(this) {
        Direction.NORTH -> Position(0, -1)
        Direction.EAST -> Position(1, 0)
        Direction.SOUTH -> Position(0, 1)
        Direction.WEST -> Position(-1, 0)
    }
}

typealias Grid = MutableList<MutableList<Object>>

fun Grid.display() {
    this.forEach {
        it.forEach { obj -> print(obj.representation()) }
        "".println()
    }
}

fun Grid.energy(): Int {
    return this.sumOf { list -> list.count { it == Object.Energized } }
}
data class Ray(val position: Position, val direction: Direction)
class GridHandler(input: List<String>) {
    private val grid: Grid = mutableListOf()
    private val gridSize: Size
    init {
        val xSize = input.first().count()
        for (string in input) {
            grid.add(string.map { Object.getObject(it) }.toMutableList())
        }
        gridSize = Size(xSize, grid.size)
    }
    fun displayGrid() {
        grid.display()
    }

    fun edgeRays(): List<Ray> {
        val south = (0 ..< gridSize.x).map { Ray(Position(it, 0), Direction.SOUTH) }
        val east = (0 ..< gridSize.y).map { Ray(Position(0, it), Direction.EAST) }
        val north = (0 ..< gridSize.x).map { Ray(Position(it, gridSize.y - 1), Direction.NORTH) }
        val west = (0 ..< gridSize.y).map { Ray(Position(gridSize.x - 1, it), Direction.WEST) }
        return south + east + north + west
    }

    fun beamsOn(initialRay: Ray = Ray(Position(0, 0), Direction.EAST)) : Grid {
        var rays = mutableListOf(initialRay)
        val directionGrid: MutableList<Ray> = mutableListOf()
        val energizedGrid: Grid = grid.map { it.mutableCopyOf() }.toMutableList()
        while(rays.isNotEmpty()) {
            rays
                .filter { it !in directionGrid }
                .onEach {
                    directionGrid.add(it)
                    energizedGrid[it.position.y][it.position.x] = Object.Energized
                }
                .flatMap { ray(it) }
                .toMutableList()
                .also { rays = it }
        }

        return energizedGrid
    }
    private fun ray(ray: Ray): List<Ray> {
        val obj = ray.position
        val directions = grid[obj.y][obj.x].nextDirection(ray.direction)
        val rays = mutableListOf<Ray>()
        for (direction in directions) {
            val expectedPosition = obj + direction.translation()
            // "Position: ${obj.description()} | obj: ${grid[obj.y][obj.x]} | initialDirection: ${ray.direction} | ExpectedPosition: ${expectedPosition.description()} | $directions".println()
            if (expectedPosition.fitsIn(gridSize)) {
                rays.add(Ray(expectedPosition, direction))
            }
        }
        return rays.toList()
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        val grid = GridHandler(input)
        grid.displayGrid()
        return grid.beamsOn().energy()
    }

    fun part2(input: List<String>): Int {
        var maxEnergy = Int.MIN_VALUE
        val grid = GridHandler(input)
        grid.displayGrid()
        for (ray in grid.edgeRays()) {
            val energizedGrid = grid.beamsOn(ray)
            // energizedGrid.display()
            maxEnergy = max(maxEnergy, energizedGrid.energy())
        }

        return maxEnergy
    }

    val testInput = readInput("Day16/Day16_test")
  //  check(part1(testInput) == 1)

    val input = readInput("Day16/Day16")
  //  part1(testInput).println()
  //   part1(input).println()
    //  part2(testInput).println()
     part2(input).println()
}
