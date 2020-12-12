import java.io.File

fun main() {
    val waitingArea =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input11.txt").readLines()

    partOne(waitingArea)
    partTwo(waitingArea)

}

fun partOne(waitingArea: List<String>){
    val snapshots = mutableListOf<List<String>>();
    var workingArea = waitingArea.toMutableList()
    val resultArea = waitingArea.toMutableList()
    val isOccupiedFunction = { x: Int, y: Int, xIncrement: Int, yIncrement: Int, seats: List<String> ->
        getSeat(x + xIncrement, y + yIncrement, seats) == '#'
    }

    while (snapshots.size < 2 || snapshots.last() != snapshots[snapshots.size - 2]) {
        for (y in waitingArea.indices) {
            for (x in waitingArea[y].indices) {
                occupyRule(x, y, workingArea, resultArea, isOccupiedFunction)
                freeRule(x, y, workingArea, resultArea, isOccupiedFunction, 4)
            }
        }
        snapshots.add(resultArea.toMutableList())
        workingArea = resultArea.toMutableList()
    }

    val result = snapshots.last().joinToString().count { it == '#' }
    println("Part 1: $result")
}

fun partTwo(waitingArea: List<String>){
    val snapshots = mutableListOf<List<String>>();
    var workingArea = waitingArea.toMutableList()
    val resultArea = waitingArea.toMutableList()
    val isOccupiedFunction = { x: Int, y: Int, xIncrement: Int, yIncrement: Int, seats: List<String> ->
        isOccupied(x, y, xIncrement, yIncrement, seats)
    }

    while (snapshots.size < 2 || snapshots.last() != snapshots[snapshots.size - 2]) {
        for (y in waitingArea.indices) {
            for (x in waitingArea[y].indices) {
                occupyRule(x, y, workingArea, resultArea, isOccupiedFunction)
                freeRule(x, y, workingArea, resultArea, isOccupiedFunction, 5)
            }
        }
        //println(resultArea)
       // println()
        snapshots.add(resultArea.toMutableList())
        workingArea = resultArea.toMutableList()
    }

    val result = snapshots.last().joinToString().count { it == '#' }
    println("Part 2: $result")
}

fun occupyRule(
    x: Int,
    y: Int,
    previous: List<String>,
    current: MutableList<String>,
    isOccupiedFunction: (x: Int, y: Int, xIncrement: Int, yIncrement: Int, seats: List<String>) -> Boolean
) {
    val seat = getSeat(x, y, previous)
    if (seat == '.' || seat == '#') {
        return
    }

    if (
        !isOccupiedFunction(x, y, -1, -1, previous) &&
        !isOccupiedFunction(x, y, 0, -1, previous) &&
        !isOccupiedFunction(x, y, +1, -1, previous) &&
        !isOccupiedFunction(x, y, +1, 0, previous) &&
        !isOccupiedFunction(x, y, +1, +1, previous) &&
        !isOccupiedFunction(x, y, 0, +1, previous) &&
        !isOccupiedFunction(x, y, -1, +1, previous) &&
        !isOccupiedFunction(x, y, -1, 0, previous)
    ) {
        setSeat(x, y, current, '#')
    }
}

fun freeRule(
    x: Int,
    y: Int,
    previous: MutableList<String>,
    current: MutableList<String>,
    isOccupiedFunction: (x: Int, y: Int, xIncrement: Int, yIncrement: Int, seats: List<String>) -> Boolean,
    toleranceThreshold: Int
) {
    val seat = getSeat(x, y, previous)
    if (seat == '.' || seat == 'L') {
        return
    }

    val checkList = mutableListOf<Boolean>()
    checkList.add(isOccupiedFunction(x, y, -1, -1, previous))
    checkList.add(isOccupiedFunction(x, y, 0, -1, previous))
    checkList.add(isOccupiedFunction(x, y, +1, -1, previous))
    checkList.add(isOccupiedFunction(x, y, +1, 0, previous))
    checkList.add(isOccupiedFunction(x, y, +1, +1, previous))
    checkList.add(isOccupiedFunction(x, y, 0, +1, previous))
    checkList.add(isOccupiedFunction(x, y, -1, +1, previous))
    checkList.add(isOccupiedFunction(x, y, -1, 0, previous))
    if (checkList.filter { it }.count() >= toleranceThreshold) {
        setSeat(x, y, current, 'L')
    }
}

fun setSeat(x: Int, y: Int, seats: MutableList<String>, value: Char) {
    seats[y] = seats[y].substring(0, x) + value + seats[y].substring(x + 1)
}

fun getSeat(x: Int, y: Int, seats: List<String>): Char {
    return if (y < 0 || y >= seats.size || x < 0 || x >= seats[y].length) {
        'X'
    } else {
        seats[y][x]
    }
}

fun isOccupied(x: Int, y: Int, xIncrement: Int, yIncrement: Int, seats: List<String>): Boolean {
    return when (getSeat(x+xIncrement, y+yIncrement, seats)) {
        'X' -> false
        '#' -> true
        'L' -> false
        else -> isOccupied(x + xIncrement, y + yIncrement, xIncrement, yIncrement, seats)
    }
}
