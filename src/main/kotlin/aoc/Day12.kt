package aoc

import java.io.File
import kotlin.math.absoluteValue

data class WaypointShip(var x: Int, var y: Int, var waypointRelativeX: Int, var waypointRelativeY: Int) {
    fun execute(instruction: NavigationInstruction) {
        move(instruction.direction, instruction.value)
    }

    private fun move(toDirection: Char, value: Int) {
        when (toDirection) {
            'W' -> waypointRelativeX -= value
            'S' -> waypointRelativeY -= value
            'E' -> waypointRelativeX += value
            'N' -> waypointRelativeY += value
            'L' -> changeDirection(toDirection, value)
            'R' -> changeDirection(toDirection, value)
            'F' -> repeat(value) {
                y += waypointRelativeY
                x += waypointRelativeX
            }
        }
    }

    private fun changeDirection(newDirection: Char, value: Int) {
        repeat(value / 90) {
            when (newDirection) {
                'L' -> {
                    val oldY = waypointRelativeY
                    waypointRelativeY = waypointRelativeX
                    waypointRelativeX = -oldY
                }
                'R' -> {
                    val oldY = waypointRelativeY
                    waypointRelativeY = -waypointRelativeX
                    waypointRelativeX = oldY
                }
            }
        }
    }
}


data class Ship(var direction: Char, var x: Long, var y: Long) {

    fun execute(instruction: NavigationInstruction) {
        move(instruction.direction, instruction.value)
    }

    private fun move(toDirection: Char, value: Int) {
        when (toDirection) {
            'W' -> x -= value
            'S' -> y -= value
            'E' -> x += value
            'N' -> y += value
            'L' -> changeDirection(toDirection, value)
            'R' -> changeDirection(toDirection, value)
            'F' -> move(this.direction, value)
        }
    }

    private fun changeDirection(newDirection: Char, value: Int) {
        repeat(value / 90) {
            when (newDirection) {
                'L' -> when (direction) {
                    'W' -> direction = 'S'
                    'S' -> direction = 'E'
                    'E' -> direction = 'N'
                    'N' -> direction = 'W'
                }
                'R' -> when (direction) {
                    'W' -> direction = 'N'
                    'S' -> direction = 'W'
                    'E' -> direction = 'S'
                    'N' -> direction = 'E'
                }
            }
        }
    }
}

data class NavigationInstruction(val direction: Char, val value: Int)

fun main() {
    val instructions =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input12.txt").readLines()
            .map { toInstruction(it) }

    val ship = Ship('E', 0, 0)
    instructions.forEach {
        ship.execute(it)
    }
    println(ship.x.absoluteValue + ship.y.absoluteValue)

    val waypointShip = WaypointShip(0,0,10, 1)
    instructions.forEach {
        waypointShip.execute(it)
    }
    println(waypointShip.x.absoluteValue + waypointShip.y.absoluteValue)
}

fun toInstruction(input: String) = NavigationInstruction(
    direction = input[0],
    value = input.substring(1).toInt()
)
