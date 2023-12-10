package day10

import println
import readInput
import kotlin.math.max
import kotlin.math.min


class Translation(val x: Int, val y:Int, val direction: Direction? = null) {
}
interface PipeDirection {
    fun conductTranslation(provenance: Direction): Translation
}

enum class Direction {
    NORTH, EAST, SOUTH, WEST
}
enum class PipeType(val char: Char) : PipeDirection {
    VERTICALPIPE('|') {
        override fun conductTranslation(provenance: Direction): Translation {
            return when(provenance) {
                Direction.NORTH -> Translation(0,1, Direction.NORTH)
                Direction.EAST -> Translation(0,0)
                Direction.SOUTH -> Translation(0,-1, Direction.SOUTH)
                Direction.WEST -> Translation(0,0)
            }
        }
    },
    HORIZONTALPIPE('-') {
        override fun conductTranslation(provenance: Direction): Translation {
            return when(provenance) {
                Direction.NORTH -> Translation(0,0)
                Direction.EAST -> Translation(-1,0, Direction.EAST)
                Direction.SOUTH -> Translation(0,0)
                Direction.WEST -> Translation(1,0, Direction.WEST)
            }
        }
    },
    NEBEND('L') {
        override fun conductTranslation(provenance: Direction): Translation {
            return when(provenance) {
                Direction.NORTH -> Translation(1,0, Direction.WEST)
                Direction.EAST -> Translation(0,-1, Direction.SOUTH)
                Direction.SOUTH -> Translation(0,0)
                Direction.WEST -> Translation(0,0)
            }
        }
    },
    NWBEND('J') {
        override fun conductTranslation(provenance: Direction): Translation {
            return when(provenance) {
                Direction.NORTH -> Translation(-1,0, Direction.EAST)
                Direction.EAST -> Translation(0,0)
                Direction.SOUTH -> Translation(0,0)
                Direction.WEST -> Translation(0,-1, Direction.SOUTH)
            }
        }
    },
    SWBEND('7') {
        override fun conductTranslation(provenance: Direction): Translation {
            return when(provenance) {
                Direction.NORTH -> Translation(0,0)
                Direction.EAST -> Translation(0,0)
                Direction.SOUTH -> Translation(-1,0, Direction.EAST)
                Direction.WEST -> Translation(0,1, Direction.NORTH)
            }
        }
    },
    SEBEND('F') {
        override fun conductTranslation(provenance: Direction): Translation {
            return when(provenance) {
                Direction.NORTH -> Translation(0,0)
                Direction.EAST -> Translation(0,1, Direction.NORTH)
                Direction.SOUTH -> Translation(1,0, Direction.WEST)
                Direction.WEST -> Translation(0,0)
            }
        }
    },
    GROUND('.') {
        override fun conductTranslation(provenance: Direction): Translation {
            return when(provenance) {
                Direction.NORTH -> Translation(0,0)
                Direction.EAST -> Translation(0,0)
                Direction.SOUTH -> Translation(0,0)
                Direction.WEST -> Translation(0,0)
            }
        }
    },
    ANIMAL('S') {
        override fun conductTranslation(provenance: Direction): Translation {
            return when(provenance) {
                Direction.NORTH -> Translation(0,-1, Direction.SOUTH)
                Direction.EAST -> Translation(1,0, Direction.WEST)
                Direction.SOUTH -> Translation(0,1, Direction.NORTH)
                Direction.WEST -> Translation(-1,0, Direction.EAST)
            }
        }
    };

    companion object {
        fun from(char: Char): PipeType = PipeType
            .entries
            .first { it.char == char }
           // .also { "${it.char} == $char = $it".println() }
    }
}
class PipeNode(val type: PipeType, val position: Position) {
    fun nextNodePosition(direction: Direction): Position {
        val translation = type.conductTranslation(direction)
        val newPosition = position.plus(translation)
        """
            POSITION: ${position.x}, ${position.y}
            TRANSLATION: ${translation.x}, ${translation.y}
            NEXTPOSITION: ${newPosition.x}, ${newPosition.y}
        """.println()
        return newPosition
    }

    fun nextNodeDirection(direction: Direction): Direction? {
        // "NEXTNODE: $type $direction  ${type.conductTranslation(direction).direction}".println()
        return type.conductTranslation(direction).direction
    }
}

typealias Maze = MutableList<MutableList<PipeNode>>

class Position(val x: Int, val y: Int) {
    fun plus(translation: Translation): Position {
        return Position(x + translation.x, y + translation.y)
    }
}
fun Maze.findAnimal(): PipeNode {
    for ((indexL, line) in this.withIndex()) {
        for((indexC, column) in line.withIndex()) {
            if (column.type == PipeType.ANIMAL) {
                "$indexL, $indexC".println()
                return PipeNode(PipeType.ANIMAL, Position(indexC, indexL))
            }
        }
    }
    throw IllegalStateException("No animals")
}

fun Maze.display(node: PipeNode) {
    for ((indexL, line) in this.withIndex()) {
        for((indexC, column) in line.withIndex()) {
            if (node.position.x == indexC && node.position.y == indexL) {
                print("O")
            } else {
                print("${column.type.char}")
            }
        }
        kotlin.io.println()
    }
}

fun Maze.nextNode(node: PipeNode, direction: Direction): Pair<PipeNode, Direction?> {
    val position = node.nextNodePosition(direction)
    if (position.y < 0 || position.y > this.count() ||
        position.x < 0 || position.x > this[position.y].count()) {
        throw IllegalAccessError("Position is out of Maze")
    }
    val nextNode = this[position.y][position.x]
    val nextNodeProvenance = node.nextNodeDirection(direction)
   // "NEXTNODE: (${nextNode.position.x},${nextNode.position.y}) Provenance: $direction NextNodeProvenance: ${nextNodeProvenance} Type: ${nextNode.type}".println()
    return Pair(nextNode, nextNodeProvenance)
}
fun Maze.pathFor(animal: PipeNode, direction: Direction): Int {
    var steps = 1
    var currentDirection: Direction? = direction
    var currentNode = animal // this[initialPipe.x][initialPipe.y]
    "----------------------------- NEW PATH -------------------------------".println()

    while (true) {
       // "DIRECTION $currentDirection".println()
       // "CURRENT NODE: ${currentNode.type}".println()
       // this.display(currentNode)

       if (currentDirection != null) {
           try {
               val (node, direction) = this.nextNode(currentNode, currentDirection)
               currentNode = node
               currentDirection = direction
           } catch (e: IllegalAccessError) {
               return 0
           }
       } else {
           return steps
       }

        if (currentNode.type == PipeType.ANIMAL) {
            // Full loop
            return steps
        }

       steps++
   }
}
fun main() {

    val symbolR = """([|\-LJ7F.S])""".toRegex()
    val allLines: Maze = emptyList<MutableList<PipeNode>>().toMutableList()
    fun part1(input: List<String>): Int {
        for ((index, string) in input.withIndex()) {
            val symbols = symbolR.findAll(string)
                .withIndex()
                .map { PipeNode(PipeType.from(it.value.value.toCharArray().first()), Position(it.index, index)) }
                .toMutableList()
            allLines.add(symbols)
        }
        allLines.findAnimal()
        var count: Int = Int.MIN_VALUE
     //   Direction.entries.forEach {
     //       try {
     //           count = max(count, allLines.pathFor(allLines.findAnimal(), it))
     //       } catch (e: IllegalStateException) {}
     //   }
         count = allLines.pathFor(allLines.findAnimal(), Direction.SOUTH)


        // allLines.display()

        return count / 2
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val inputString =
   //     "Day10/Day10_test"
        "Day10/Day10_test2"
    val testInput = readInput(inputString)
   // check(part1(testInput) == 4)
    val input = readInput("Day10/Day10")
   // part1(testInput).println()
     part1(input).println()
  //  part2(input).println()
}
