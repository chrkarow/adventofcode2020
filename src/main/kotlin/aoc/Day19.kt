package aoc

import java.io.File

interface MessageRule {
    fun getRegex(): String
}

class CharacterRule(private val character: Char) : MessageRule {

    override fun getRegex(): String {
        return character.toString()
    }
}

class CompositeRule : MessageRule {

    private val childRules = mutableListOf<List<MessageRule>>()
    private var result: String? = null

    fun addChildRules(rules: List<MessageRule>) {
        childRules.add(rules)
    }

    override fun getRegex(): String {
        return result ?: childRules
            .joinToString(
                separator = "|",
                prefix = "(",
                postfix = ")"
            ) {
                it.joinToString(separator = "") { rule ->
                    if (rule == this) {
                        "+"
                    } else {
                        rule.getRegex()
                    }
                }
            }
            .let {
                if (it.last() == '+') {
                    it.drop(1).dropLast(2) + "+"
                } else {
                    it
                }
            }
            .also { result = it }
    }

}

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input19.txt").readLines()
    val rules = lines.subList(0, lines.indexOf(""))
    val messages = lines.subList(lines.indexOf(""), lines.size)

    // Part 1
    val indexedRules = createRules(rules)
    val regex = "^${indexedRules[0]!!.getRegex()}$".toRegex()
    val result = messages.filter {
        regex.matches(it)
    }.count()
    println(result)

    // PArt 2 - ultra stupid try and error, hacky as fuck. Takes into account that 0: 8 11 (rule 8 at the start, the rule 11)
    val changeableRules = rules.toMutableList()
    changeableRules[changeableRules.indexOf("8: 42")] = "8: 42 8"
    changeableRules[changeableRules.indexOf("11: 42 31")] = "11: \"Y\""
    val indexedRules2 = createRules(changeableRules)
    val regex2 = "^${indexedRules2[0]!!.getRegex()}".replace("Y", "").toRegex()
    var result2 = 0
    val restRegex = "$regex2${indexedRules2[42]!!.getRegex()}{X}${indexedRules2[31]!!.getRegex()}{X}$"
    (1..20).forEach { i ->
        val replaced = restRegex.replace("X", "$i").toRegex()
        println(replaced)
        result2 += messages.filter {
            replaced.matches(it)
        }.count()
    }
    println(result2)

}

fun createRules(rules: List<String>): Map<Int, MessageRule> {
    val returnMap = mutableMapOf<Int, MessageRule>()
    val ruleStrings = rules.map { it.replace("\"", "") }.toMutableList()
    while (ruleStrings.isNotEmpty()) {

        val removeItems = mutableListOf<String>()
        ruleStrings.forEach { ruleString ->
            val split = ruleString.split(":")
            val index = split.first().toInt()
            val rawRule = split[1].trim()

            if (rawRule.first().isLetter()) {
                returnMap[index] = CharacterRule(rawRule.first())
                removeItems.add(ruleString)

            } else {
                val referencedRules = rawRule
                    .split("|")
                    .map { subRules ->
                        subRules
                            .trim()
                            .split(" ")
                            .map(String::toInt)
                    }

                if (returnMap.keys.containsAll(referencedRules.flatten().filter { it != index })) {
                    val compositeRule = CompositeRule()
                    referencedRules.forEach { indices ->
                        compositeRule.addChildRules(indices.map {
                            if (it == index) {
                                compositeRule
                            } else {
                                returnMap[it]!!
                            }
                        })
                    }
                    returnMap[index] = compositeRule
                    removeItems.add(ruleString)
                }
            }
        }
        ruleStrings.removeAll(removeItems)
    }
    return returnMap
}
