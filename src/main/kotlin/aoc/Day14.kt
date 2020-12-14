package aoc

import java.io.File
import kotlin.math.pow

fun main() {
    val instructions =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input14.txt").readLines()
    part1(instructions)
    part2(instructions)
}

fun part1(instructions: List<String>) {
    var mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    val memory = mutableMapOf<String, String>()
    instructions.forEach {
        val split = it
            .replace(" ", "")
            .replace("[", "")
            .replace("]", "")
            .split("=")
        if (split[0] == "mask") {
            mask = split[1]
        } else {
            val address = split[0].substring(3)
            val binary = toBinary(split[1].toLong())
            val masked = applyMask(binary, mask)
            memory[address] = masked
        }
    }
    val result = memory.entries.map { toNumber(it.value) }.sum()
    println(result)

}

fun part2(instructions: List<String>) {
    var mask = "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
    val memory = mutableMapOf<String, Long>()
    instructions.forEach {
        val split = it
            .replace(" ", "")
            .replace("[", "")
            .replace("]", "")
            .split("=")
        if (split[0] == "mask") {
            mask = split[1]
        } else {
            val address = toBinary(split[0].substring(3).toLong())
            val maskedAddress = applyMemoryMask(address, mask)
            writeToMaskedAddress(maskedAddress, split[1].toLong(), memory)
        }
    }
    val result = memory.entries.map { it.value }.sum()
    println(result)

}

fun writeToMaskedAddress(maskedAddress: String, value: Long, memory: MutableMap<String, Long>) {
    if (!maskedAddress.contains("X")) {
        memory[maskedAddress] = value
    } else {
        val indexOfLast = maskedAddress.indexOfLast { it == 'X' }
        val newAddress1 = maskedAddress.substring(0, indexOfLast) + "1" + maskedAddress.substring(indexOfLast + 1)
        writeToMaskedAddress(newAddress1, value, memory)
        val newAddress0 = maskedAddress.substring(0, indexOfLast) + "0" + maskedAddress.substring(indexOfLast + 1)
        writeToMaskedAddress(newAddress0, value, memory)
    }
}

fun applyMemoryMask(binaryString: String, mask: String): String {
    val sb = StringBuilder(binaryString)
    for (i in mask.indices) {
        if (mask[i] == '0') {
            sb[i] = binaryString[i]
        } else {
            sb[i] = mask[i]
        }
    }
    return sb.toString()
}

fun applyMask(binaryString: String, mask: String): String {
    val sb = StringBuilder(binaryString)
    for (i in mask.indices) {
        if (mask[i] != 'X') {
            sb[i] = mask[i]
        }
    }
    return sb.toString()
}

fun toNumber(value: String): Long {
    val reversed = value.reversed()
    var i = 0
    var result = 0L
    reversed.forEach {
        result += (2.toDouble().pow(i) * it.toString().toInt()).toLong()
        i++
    }
    return result
}

fun toBinary(number: Long): String {
    var workingNumber = number
    val binaryStr = StringBuilder()

    while (workingNumber > 0) {
        val r = workingNumber % 2
        workingNumber /= 2
        binaryStr.append(r)
    }

    return binaryStr.reverse().padStart(36, '0').toString()
}
