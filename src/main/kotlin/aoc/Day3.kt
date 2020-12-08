import java.io.File

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input3.txt").readLines()

    val results = mutableListOf<Int>()
    results.add(slopeDown(1, 1, lines))
    results.add(slopeDown(3, 1, lines))
    results.add(slopeDown(5, 1, lines))
    results.add(slopeDown(7, 1, lines))
    results.add(slopeDown(1, 2, lines))
    println(results)
    println(results.fold(1L) { acc, i -> acc * i })
}

fun slopeDown(right: Int, down: Int, lines: List<String>): Int {
    var treeCount = 0
    var y = 0
    var x = 0
    while (y < lines.size) {
        if (lines[y][correctIndex(x, lines[y].length)] == '#') {
            treeCount++
        }
        x += right
        y += down
    }
    return treeCount
}

fun correctIndex(index: Int, length: Int): Int {
    val i = if (index < 0) length - index else index
    return i % length
}
