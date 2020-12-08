package aoc

import java.io.File

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input5.txt").readLines()
    println(lines.maxOf { locateSeat(it) })

    val filledSeats = lines.map { locateSeat(it) }.filter { it in 8..1015 }.sorted()
    var leftNeighbour = 0
    while (filledSeats[leftNeighbour + 1] - filledSeats[leftNeighbour] == 1) {
        leftNeighbour++
    }
    println("Left: ${filledSeats[leftNeighbour]}")

    var rightNeighbour = filledSeats.size - 1
    while (filledSeats[rightNeighbour] - filledSeats[rightNeighbour - 1] == 1) {
        rightNeighbour--
    }
    println("Right ${filledSeats[rightNeighbour]}")
}

fun locateSeat(boardingPass: String): Int {
    val rowIdentifier = boardingPass.substring(0..6)
    val colIdentifier = boardingPass.substring(7 until boardingPass.length)

    return locateRow(rowIdentifier) * 8 + locateCol(colIdentifier)
}

fun locateCol(colIdentifier: String): Int {
    var cols = (0..7).toList()
    colIdentifier.forEach {
        println("Possible cols: $cols")
        when (it) {
            'L' -> cols = cols.take(cols.size / 2)
            'R' -> cols = cols.takeLast(cols.size / 2)
        }
    }
    println("Selected col ${cols[0]}")
    return cols[0]
}

fun locateRow(rowIdentifier: String): Int {
    var rows = (0..127).toList()
    rowIdentifier.forEach {
        println("Possible rows: $rows")
        when (it) {
            'F' -> rows = rows.take(rows.size / 2)
            'B' -> rows = rows.takeLast(rows.size / 2)
        }
    }
    println("Selected row ${rows[0]}")
    return rows[0]
}
