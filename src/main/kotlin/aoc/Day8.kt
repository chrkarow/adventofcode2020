package aoc

import java.io.File
import java.lang.RuntimeException

data class ExecutionResult(val result: Int, val infiniteLoopDetected: Boolean)

data class Instruction(val lineNo: Int, val code: String, val increment: Int) {
    var executed = false

    fun execute(acc: Int, instructions: List<Instruction>): ExecutionResult {
        if (executed) {
            return ExecutionResult(acc, true)
        }

        val newIndex = when (code) {
            "nop" -> lineNo + 1
            "acc" -> lineNo + 1
            "jmp" -> lineNo + increment
            else -> throw RuntimeException("Kaputt !")
        }

        val newAcc = when (code) {
            "nop" -> acc
            "acc" -> acc + increment
            "jmp" -> acc
            else -> throw RuntimeException("Kaputt !")
        }

        if(newIndex == instructions.size){
            return ExecutionResult(newAcc, false)
        }

        executed = true
        return instructions[newIndex].execute(newAcc, instructions)
    }
}

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input8.txt").readLines()
    val instructions = toInstructions(lines)

    val result = instructions[0].execute(0, deepCopy(instructions))
    println("part 1: ${result.result}")

    var execution = ExecutionResult(0, true)
    val replaceCandidates = permuteInstructions(deepCopy(instructions))
    var i = 0
    while (execution.infiniteLoopDetected) {
        val candidate = replaceCandidates[i]
        val mutableInstructions = deepCopy(instructions).toMutableList()
        mutableInstructions[candidate.lineNo] = candidate
        execution = mutableInstructions[0].execute(0, mutableInstructions)
        i++
    }

    println("part 2: ${execution.result}")
}

fun deepCopy(instructions: List<Instruction>): List<Instruction> {
    return instructions.map { it.copy() }
}

fun permuteInstructions(instructions: List<Instruction>): List<Instruction> {
    val returnList = mutableListOf<Instruction>()
    for (i in instructions.size - 1 downTo 0) {
        if (instructions[i].code == "nop") {
            returnList.add(instructions[i].copy(code = "jmp"))
        } else if (instructions[i].code == "jmp") {
            returnList.add(instructions[i].copy(code = "nop"))
        }
    }
    return returnList
}

fun toInstructions(lines: List<String>): List<Instruction> {
    val instructions = mutableListOf<Instruction>()
    for (i in lines.indices) {
        instructions.add(toInstruction(i, lines[i]))
    }
    return instructions
}

fun toInstruction(lineNo: Int, line: String): Instruction {
    val tokens = line.split(" ")
    return Instruction(lineNo, tokens[0], tokens[1].toInt())
}
