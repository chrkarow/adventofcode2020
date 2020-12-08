package aoc

import java.io.File
import java.io.FileInputStream

// Identification code 1216993-20201207-c2bc3140
fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input1.txt").readLines().map { it.toInt() }
    partOne(lines)
    partTwo(lines)
}

fun partOne(numbers: List<Int>) {
    for (i in 0 until numbers.size - 2) {
        for (j in i + 1 until numbers.size) {
            if (numbers[i] + numbers[j] == 2020) {
                println("Number 1: ${numbers[i]}, Number 2: ${numbers[j]}")
                println("Result: ${numbers[i] * numbers[j]}")
                return
            }
        }
    }
}

fun partTwo(numbers: List<Int>) {
    for (i in 0 until numbers.size - 3) {
        for (j in i + 1 until numbers.size - 2) {
            for (k in i + 2 until numbers.size - 1) {
                if (numbers[i] + numbers[j] + numbers[k] == 2020) {
                    println("Number 1: ${numbers[i]}, Number 2: ${numbers[j]}, Number 3: ${numbers[k]}")
                    println("Result: ${numbers[i] * numbers[j] * numbers[k]}")
                    return
                }
            }
        }
    }
}
