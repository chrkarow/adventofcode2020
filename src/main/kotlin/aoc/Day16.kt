package aoc

import java.io.File

class TicketRule(val name: String, val ranges: List<IntRange>) {

    fun isValid(number: Int) = ranges
        .map { range -> number in range }
        .reduce(Boolean::or)

    override fun toString(): String = name
}

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input16.txt").readLines()


    val startMyTicket = lines.indexOf("your ticket:")
    val startNearbyTickets = lines.indexOf("nearby tickets:")

    val rules = lines.subList(0, startMyTicket - 1).map { parseTicketRule(it) }
    val myTicket = lines[startMyTicket + 1]
        .split(",")
        .map { it.toInt() }
    val nearbyTickets = lines
        .subList(startNearbyTickets + 1, lines.size)
        .map {
            it
                .split(",")
                .map { value -> value.toInt() }
        }

    // Part 1
    val result1 = nearbyTickets
        .flatten()
        .filter { number ->
            !rules
                .map { rule -> rule.isValid(number) }
                .reduce(Boolean::or)
        }.sum()
    println(result1)


    // Part 2
    val validTickets = nearbyTickets
        .filter { isValidTicket(it, rules) }


    val indexedRules = determineIndexes(rules, validTickets)

    println(indexedRules)

    val result2 = indexedRules.entries
        .filter { it.key.name.startsWith("departure") }
        .map { myTicket[it.value].toLong() }
        .reduce(Long::times)
    println(result2)
}

 fun determineIndexes(
    rules: List<TicketRule>,
    validTickets: List<List<Int>>
): Map<TicketRule, Int> {
    val indexedRules = rules
        .map {
            Pair(it, determineIndexCandidates(it, validTickets).toMutableList())
        }.sortedBy { it.second.size }

    for (i in indexedRules.indices) {
        val pair = indexedRules[i]
        if (pair.second.size == 1) {
            for (j in i + 1 until indexedRules.size) {
                indexedRules[j].second.remove(pair.second.first())
            }
        }
    }

    return indexedRules.map { Pair(it.first, it.second[0]) }.toMap()
}

fun isValidTicket(ticket: List<Int>, rules: List<TicketRule>) =
    !ticket
        .map { number ->
            rules
                .map { rule -> rule.isValid(number) }
                .reduce(Boolean::or)
        }.contains(false)


fun determineIndexCandidates(rule: TicketRule, tickets: List<List<Int>>): List<Int> {
    val indexCandidates = mutableListOf<Int>()
    for (i in tickets[0].indices) {

        if (tickets
                .map {
                    rule.isValid(it[i])
                }.reduce(Boolean::and)
        ) {
            indexCandidates.add(i)
        }
    }
    return indexCandidates
}

fun parseTicketRule(ruleString: String): TicketRule {
    val ruleParts = ruleString.split(":")

    val ranges = ruleParts[1]
        .replace(" ", "")
        .split("or")
        .map {
            val bounds = it.split("-")
            bounds[0].toInt()..bounds[1].toInt()
        }
    return TicketRule(
        name = ruleParts[0],
        ranges = ranges
    )
}
