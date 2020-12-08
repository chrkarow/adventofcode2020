package aoc

import java.io.File

class PasswordPolicy(
    value: String
) {

    private val min: Int
    private val max: Int
    private val character: Char

    init {
        val split = value.split("-")
        min = split[0].toInt()
        val split2 = split[1].split(" ")
        max = split2[0].removePrefix("-").toInt()
        character = split2[1].toCharArray()[0]
    }

    fun isValid(pw: String): Boolean {
        val count = pw.filter { it == character }.count()
        return count in min..max
    }

    fun isValid2(pw: String): Boolean {
        return (pw[min - 1] == character).xor(pw[max - 1] == character)
    }
}

fun main() {
    val pwp =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input2.txt").readLines().map {
            val split = it.split(": ")
            Pair(split[1], PasswordPolicy(split[0]))
        }

    println("${pwp.filter { it.second.isValid(it.first) }.count()}")
    println("${pwp.filter { it.second.isValid2(it.first) }.count()}")
}
