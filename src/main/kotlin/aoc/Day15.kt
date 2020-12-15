package aoc

val puzzleInput = listOf(2L, 0L, 1L, 9L, 5L, 19L)
//val puzzleInput = listOf(3,1,2)

fun main() {
    val memory = mutableMapOf<Long, Long>()
    var turn = 1L
    var lastNumber = 0L
    repeat(30000000) {
        if (turn - 1 in puzzleInput.indices) {
            lastNumber = puzzleInput[(turn - 1).toInt()]
            memory[lastNumber] = turn
        } else {
            val spokenInRound = memory[lastNumber]
            if (spokenInRound != null) {
                memory[lastNumber] = turn - 1
                lastNumber = turn - 1 - spokenInRound
            } else {
                memory[lastNumber] = turn-1
                lastNumber = 0
            }
        }

        turn++
    }
    println(lastNumber)
}
