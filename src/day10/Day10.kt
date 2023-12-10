package day10

import println
import readInput
class Translation(val x: Int, val y:Int, val direction: Direction? = null)
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
    },
    INSIDE('I') {
        override fun conductTranslation(provenance: Direction): Translation {
            TODO("Not yet implemented")
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
      //  """
      //      POSITION: ${position.x}, ${position.y}
      //      TRANSLATION: ${translation.x}, ${translation.y}
      //      NEXTPOSITION: ${newPosition.x}, ${newPosition.y}
      //  """.println()
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
fun Maze.display(node: PipeNode? = null) {
    for ((indexL, line) in this.withIndex()) {
        for((indexC, column) in line.withIndex()) {
            if (node != null && node.position.x == indexC && node.position.y == indexL) {
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
fun Maze.pathCountFor(animal: PipeNode, direction: Direction): Int {
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
fun Maze.pathFor(animal: PipeNode, direction: Direction): MutableList<PipeNode> {
    var path = mutableListOf<PipeNode>()
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
               throw e
           }

           path.add(currentNode)
           if (currentNode.type == PipeType.ANIMAL) {
               // Full loop
               return path
           }
       } else {
          throw IllegalStateException("Wrong path")
       }
   }
}

typealias Path = MutableList<PipeNode>

// fun Path.isPointEnclosed(node: PipeNode): Boolean {
//     // https://en.wikipedia.org/wiki/Even%E2%80%93odd_rule
//     var res = false
//     if (this.contains(node)) {
//         return true
//     }
//     for (point in this) {
//
//     }
// }

fun Path.isPointInPath(node: PipeNode): Boolean {
    var count = 0
    val n = this.size
    val point = node.position

    for (i in this.indices) {
        val p1 = this[i].position
        val p2 = this[(i + 1) % n].position

        // Check if point.y is between p1.y and p2.y
        if (p1.y > point.y != p2.y > point.y) {
            val intersectX = p1.x + (point.y - p1.y) * (p2.x - p1.x) / (p2.y - p1.y)
            if (point.x < intersectX) {
                count++
            }
        }
    }

    return count % 2 != 0
}
fun Maze.pointsEnclosedIn(path: MutableList<PipeNode>): Int {
    var count = 0
    var enclosedPoints = 0
    var mazeCopy = this
    for ((indexL, line) in this.withIndex()) {
        for((indexC, column) in line.withIndex()) {
            val node = this[indexL][indexC]
            if (path.contains(node)) {
               count++
                if (node.type == PipeType.SWBEND ||
                    node.type == PipeType.SEBEND
             //   ||    node.type == PipeType.ANIMAL
                    ) {
                    count++
                }
            } else if((count % path.size) % 2 == 1 &&
                indexL != 0 && indexC != 0 && // Borders
                indexL != this.count() && // Borders
                indexC != this[indexL].count() // Borders
                ) {
                enclosedPoints++
                mazeCopy[indexL][indexC] = PipeNode(PipeType.INSIDE, column.position)
            }
           // if (path.isPointInsidePolygon(node)) {
           //     enclosedPointts++
           //     mazeCopy[indexL][indexC] = PipeNode(PipeType.INSIDE, column.position)
           // }
        }
        kotlin.io.println()
    }
    mazeCopy.display()
    return enclosedPoints
}
fun Path.isPointInsidePolygon(point: PipeNode): Boolean {
    var intersectCount: Int = 0

    for (i in this.indices) {
        var vertex1 = this[i].position
        var vertex2 = this[(i + 1) % this.size].position

        // Ensure vertex1 is the lower point of the edge
        if (vertex1.y > vertex2.y) {
            val temp = vertex1
            vertex1 = vertex2
            vertex2 = temp
        }

        // Check if the point's Y coordinate is between the Y coordinates of the edge
        if (point.position.y > vertex1.y && point.position.y <= vertex2.y) {
            // Check if the point is to the left of the edge
            if (point.position.x < maxOf(vertex1.x, vertex2.x)) {
                val xIntersection: Int

                if (vertex1.y != vertex2.y) {
                    xIntersection = (point.position.y - vertex1.y) * (vertex2.x - vertex1.x) / (vertex2.y - vertex1.y) + vertex1.x
                } else {
                    xIntersection = vertex1.x
                }

                // If the point is to the left of the intersection, increment the count
                if (point.position.x <= xIntersection) {
                    intersectCount++
                }
            }
        }
    }

    // If intersectCount is odd, the point is inside the polygon
    return intersectCount % 2 != 0
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
       // allLines.findAnimal()
        var count: Int = Int.MIN_VALUE
     //   Direction.entries.forEach {
     //       try {
     //           count = max(count, allLines.pathFor(allLines.findAnimal(), it))
     //       } catch (e: IllegalStateException) {}
     //   }
        // count = allLines.pathCountFor(allLines.findAnimal(), Direction.SOUTH)
         count = allLines.pathFor(allLines.findAnimal(), Direction.SOUTH).count()


        // allLines.display()

        return count / 2
    }
    fun part2(input: List<String>): Int {
        for ((index, string) in input.withIndex()) {
            val symbols = symbolR.findAll(string)
                .withIndex()
                .map { PipeNode(PipeType.from(it.value.value.toCharArray().first()), Position(it.index, index)) }
                .toMutableList()
            allLines.add(symbols)
        }
        // allLines.findAnimal()

        //   Direction.entries.forEach {
        //       try {
        //           count = max(count, allLines.pathFor(allLines.findAnimal(), it))
        //       } catch (e: IllegalStateException) {}
        //   }
        // count = allLines.pathCountFor(allLines.findAnimal(), Direction.SOUTH)
        allLines.display()
        val path = allLines.pathFor(allLines.findAnimal(), Direction.SOUTH)

        return allLines.pointsEnclosedIn(path)
    }

    // test if implementation meets criteria from the description, like:
    val inputString =
   //     "Day10/Day10_test"
   //     "Day10/Day10_test2"
   //     "Day10/Day10_test3"
   //     "Day10/Day10_test4"
        "Day10/Day10_test5"
    val testInput = readInput(inputString)
   // check(part1(testInput) == 4)
    val input = readInput("Day10/Day10")
   // part1(testInput).println()
   //  part1(input).println() // 6701
   // part2(testInput).println()
     check(part2(testInput) == 10)
  //  part2(input).println() // 2795 > // 2200 >
}
