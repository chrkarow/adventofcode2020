package aoc

import java.io.File

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input13.txt").readLines()

    // Part 1
    val time = lines[0].toLong()
    val busLines = lines[1].split(",").filter { it != "x" }.map { it.toInt() }

    val mapped = busLines.associateWith { it - (time % it) }
    println(mapped)
    val busLine = mapped.minByOrNull { it.value }
    println(busLine?.key?.times(busLine.value))


    // Part 2 - slow af and not really elegant but simple :D
    val schedule = lines[1].split(",").map {
        if (it == "x") {
            -1
        } else {
            it.toInt()
        }
    }

    val number = findNumber(busLines.associateWith { schedule.indexOf(it) })
    println(number)

}

fun findNumber(busLines: Map<Int, Int>): Long {
    println(busLines)
    val maxBusLine = busLines.entries.maxByOrNull { it.key }!!
    var i = maxBusLine.key - (maxBusLine.value % maxBusLine.key).toLong()
    val increment = maxBusLine.key
    while (!checkNumber(i, busLines)) {
        i += increment
    }
    return i
}

fun checkNumber(number: Long, busLines: Map<Int, Int>): Boolean {
    for (entry in busLines.entries) {
        if ((number + entry.value) % entry.key != 0L) {
            return false
        }
    }
    return true
}
