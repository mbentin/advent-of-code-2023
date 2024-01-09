enum class Direction { NORTH, EAST, SOUTH, WEST;
    fun sides(): List<Direction> {
        return when(this) {
            NORTH -> listOf(WEST, EAST)
            EAST -> listOf(NORTH, SOUTH)
            SOUTH -> listOf(WEST, EAST)
            WEST -> listOf(NORTH, SOUTH)
        }
    }
}
class Translation(val x: Int, val y:Int, val direction: Direction? = null)
data class Position(val x: Int, val y: Int) {
    operator fun plus(position: Position): Position {
        return Position(this.x + position.x, this.y + position.y)
    }
    fun fitsIn(size: Size): Boolean {
        return this.x >= 0 && this.x < size.x && this.y >= 0 && this.y < size.y
    }
    fun description(): String {
        return "($x,$y)"
    }
    fun translated(direction: Direction, step: Int = 1): Position {
        return when(direction) {
            Direction.NORTH -> Position(x, y - step)
            Direction.EAST -> Position(x + step, y)
            Direction.SOUTH -> Position(x, y + step)
            Direction.WEST -> Position(x - step, y)
        }
    }

    fun direction(position: Position): Direction {
        if (this.x > position.x) {
            return Direction.WEST
        } else if (this.x < position.x) {
            return Direction.EAST
        } else if (this.y < position.y) {
            return Direction.SOUTH
        } else if (this.y > position.y) {
            return Direction.NORTH
        }
        return Direction.SOUTH
    }
}

interface Neighboring {
    fun neighborsFor(position: Position): List<Position>
}
@JvmInline
value class Grid<T>(val grid: MutableList<MutableList<T>>) : Neighboring {
   fun size(): Size {
        return Size(grid.first().size, grid.size)
    }
    fun add(list: MutableList<T>) {
        grid.add(list)
    }
    override fun neighborsFor(position: Position): List<Position> {
        val offsets = listOf(
            Position(0, 1),
            Position(0, -1),
            Position(1, 0),
            Position(-1, 0),
        )
        return offsets.map { position.plus(it) }.filter { it.fitsIn(size()) }
    }

    fun get(position: Position): T {
        return grid[position.y][position.x]
    }
}
