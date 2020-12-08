package aoc

import java.io.File

class BagRule(val color: String, val content: Map<String, Int>) {

    override fun toString(): String {
        return "Color: $color, contents: $content"
    }

    fun canContain(bagColor: String): Boolean {
        return content.keys.contains(bagColor)
    }
}

fun main() {
    val rules =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input7.txt").readLines().map {
            parseRule(it)
        }.associateBy { it.color }

    println(countColors("shiny gold", rules.map { it.value }))
    println(countBags("shiny gold", rules) - 1) // -1 because we don't count the outer bag
}

fun countBags(color: String, rules: Map<String, BagRule>): Int {
    val rule = rules[color]!!
    println(rule)
    var sum = 1
    rule.content.forEach{
        sum += countBags(it.key, rules) * it.value
    }
    return sum
}

fun countColors(color: String, rules: List<BagRule>): Int{
    var previous = setOf<String>()
    var current = getContainingColors(color, rules)
    println(current)
    while(previous.size<current.size){
        previous = current
        current = current.union(previous.flatMap { getContainingColors(it, rules) }.toSet())
        println(current)
    }
    return current.size
}

fun getContainingColors(bagColor: String, rules: List<BagRule>): Set<String> {
    return rules.filter { it.canContain(bagColor) }.map { it.color }.toSet()
}

fun parseRule(rule: String): BagRule {
    val split = rule.split(" contain ")
    val color = split[0].removeSuffix(" bags")

    val contentColors: Map<String, Int> = split[1].removeSuffix(".").split(", ")
        .asSequence()
        .map { it.removeSuffix(" bags") }
        .map { it.removeSuffix(" bag") }
        .filter { it != "no other" }
        .map { Pair(it.substring(2), it.substring(0,1).toInt()) }
        .toMap()

    return BagRule(
        color = color,
        content = contentColors
    )
}
