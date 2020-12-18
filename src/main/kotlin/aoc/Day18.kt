package aoc

import java.io.File


fun main() {
    val terms =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input18.txt").readLines()

    val result1 = terms
        .map { it.replace(" ", "") }
        .map { calculate(it) }
        .sum()
    println(result1)

    val result2 = terms
        .map { it.replace(" ", "") }
        .map { calculate(it, true) }
        .sum()
    println(result2)
}

fun calculate(term: String, partTwo: Boolean = false): Long {
    var workingTerm = term
    var index = workingTerm.indexOfFirst { it == '(' }
    while (index >= 0) {
        val subTerm = createSubTerm(
            if (index == 0) {
                workingTerm
            } else {
                workingTerm.substring(index)
            }
        )
        val subResult = calculate(subTerm, partTwo)
        workingTerm = workingTerm.replace("($subTerm)", subResult.toString())

        index = workingTerm.indexOfFirst { it == '(' }
    }

    if (partTwo) {
        workingTerm = workingTerm.split("*").joinToString("*") { calculateClearedTerm(it).toString() }
    }
    return calculateClearedTerm(workingTerm)
}

fun calculateClearedTerm(term: String): Long {
    var result = 0L
    var operator = '+'
    var number = ""

    term.forEach {
        when {
            it.isDigit() -> number += it
            else -> {
                when (operator) {
                    '+' -> result += number.toLong()
                    '*' -> result *= number.toLong()
                }
                operator = it
                number = ""
            }
        }
    }
    when (operator) {
        '+' -> result += number.toLong()
        '*' -> result *= number.toLong()
    }

    return result
}

fun createSubTerm(term: String): String {
    var bracketCount = 1
    var index = 0
    while (bracketCount != 0) {
        index++
        when (term[index]) {
            '(' -> bracketCount++
            ')' -> bracketCount--
        }
    }
    return term.substring(1, index)
}
