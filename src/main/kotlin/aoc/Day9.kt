import java.io.File

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input9.txt").readLines().map { it.toLong() }

    var i = 25
    while (i in lines.indices && sumExists(lines[i], lines.subList(i - 25, i))) {
        i++
    }
    println(lines[i])

    println(getRange(lines[i], lines, i))
}

fun sumExists(sum: Long, numbers: List<Long>): Boolean {
    println("Sum: $sum, list: $numbers")
    numbers.forEach { summand1 ->
        numbers.forEach { summand2 ->
            if (summand1 + summand2 == sum) return true
        }
    }
    return false
}

fun getRange(number: Long, numbers: List<Long>, maxIndex: Int): Long {
    var lowerBound = 0
    while (lowerBound < maxIndex - 2) {
        var upperBound = lowerBound + 1
        while (upperBound < maxIndex) {
            val subList = numbers.subList(lowerBound, upperBound)
            val sum = subList.sum()
            println("Sum $sum, list: $subList")
            if (sum == number) {
                return subList.minOrNull()!! + subList.maxOrNull()!!
            }
            upperBound++
        }
        lowerBound++
    }
    println("No result!")
    return 0
}
