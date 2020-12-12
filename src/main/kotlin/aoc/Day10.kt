import java.io.File

fun main() {
    val sorted =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input10.txt").readLines()
            .map { it.toInt() }
            .sorted()


    println(sorted)

    val result = sorted.toMutableList().also { it.add(it.last() + 3) }
        .fold(Triple(0, 0, 0)) { acc, current ->
            val dif = current - acc.third
            when (dif) {
                1 -> Triple(acc.first + 1, acc.second, current)
                3 -> Triple(acc.first, acc.second + 1, current)
                else -> Triple(acc.first, acc.second, current)
            }
        }

    println(result.first * result.second)
    val withZero = sorted.toMutableList().also { it.add(0, 0) }

    val result2 = splitList(withZero)
        .map { count(it.first(), it.last(), it) }
        .fold(1L) { acc, v -> acc * v }
    println(result2) // Haesslich...geht aber auch!

    println(countMap(withZero)) // Extrem elegate loesung
}

fun splitList(sorted: List<Int>): List<List<Int>> {
    var i = 0
    var j = 1
    val returnList = mutableListOf<List<Int>>()
    while (j + 1 in sorted.indices) {
        if (sorted[j + 1] - sorted[j] == 3) {
            returnList.add(sorted.subList(i, j + 1))
            i = j + 1
        }
        j++
    }
    returnList.add(sorted.subList(i, sorted.size))
    return returnList
}

fun count(start: Int, end: Int, adapters: List<Int>): Long {
    //println("Start: $start, End: $end, adapters: $adapters")
    if (start == end) {
        return 1L
    }

    val c = if (adapters.contains(start + 3)) {
        count(start + 3, end, adapters)
    } else {
        0
    }

    val b = if (adapters.contains(start + 2)) {
        count(start + 2, end, adapters)
    } else {
        0
    }

    val a = if (adapters.contains(start + 1)) {
        count(start + 1, end, adapters)
    } else {
        0
    }

    return a + b + c
}

fun countMap(adapters: List<Int>): Long {
    val numbers = adapters.associateWith { 0L }.toMutableMap()

    numbers[numbers.keys.first()] = 1
    numbers.forEach {
        if (numbers[it.key + 1] != null) {
            numbers[it.key + 1] = numbers[it.key + 1]!! + it.value
        }

        if (numbers[it.key + 2] != null) {
            numbers[it.key + 2] = numbers[it.key + 2]!! + it.value
        }

        if (numbers[it.key + 3] != null) {
            numbers[it.key + 3] = numbers[it.key + 3]!! + it.value
        }
    }

    return numbers.values.last()
}
