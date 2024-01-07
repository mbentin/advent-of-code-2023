enum class Direction { NORTH, EAST, SOUTH, WEST }
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
}

@JvmInline
value class Grid<T>(val grid: MutableList<MutableList<T>>) {
   fun size(): Size {
        return Size(grid.first().size, grid.size)
    }
    fun add(list: MutableList<T>) {
        grid.add(list)
    }
    fun neighborsFor(position: Position): List<Position> {
        val offsets = listOf(
            Position(0, 1),
            Position(0, -1),
            Position(1, 0),
            Position(-1, 0),
        )
        return offsets.map { position.plus(it) }.filter { it.fitsIn(size()) }
    }

    fun get(position: Position): T {
        return grid[position.x][position.y]
    }
}
