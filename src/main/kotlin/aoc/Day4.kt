package aoc

import java.io.File


class Rule(val key: String, val isValid: (String) -> Boolean) {
    fun check(passport: Map<String, String>): Boolean {
        val result = passport[key]?.let { isValid(it) } ?: false
        println("Rule: $key, value: ${passport[key]} -> $result")

        return result
    }

}

fun main() {
    val rules = setOf(
        Rule("byr") { it.length == 4 && it.toIntOrNull()?.let { year -> year in 1920..2002 } ?: false },
        Rule("iyr") { it.length == 4 && it.toIntOrNull()?.let { year -> year in 2010..2020 } ?: false },
        Rule("eyr") { it.length == 4 && it.toIntOrNull()?.let { year -> year in 2020..2030 } ?: false },
        Rule("hgt") {
            val um = it.substring(it.length - 2)
            val number = it.substring(0..it.length - 3).toIntOrNull()
            if (number == null) {
                false
            } else {
                when (um) {
                    "cm" -> number in 150..193
                    "in" -> number in 59..76
                    else -> false
                }
            }
        },
        Rule("hcl") { it.matches("^#[0-9a-f]{6}$".toRegex()) },
        Rule("ecl") { setOf("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(it) },
        Rule("pid") { it.matches("^[0-9]{9}$".toRegex()) }
    )
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input4.txt").readLines()
    val passPorts = createPassPorts(lines)
    val result = checkPassports(passPorts, rules)
    println(result)
}

fun checkPassports(
    passports: List<Map<String, String>>,
    rules: Set<Rule>
): Int {
    println(passports)
    return passports.filter {
        rules.fold(true) { acc, rule -> acc && rule.check(it) }
    }.count()
}

fun createPassPorts(lines: List<String>): List<Map<String, String>> {
    val passports = mutableListOf<Map<String, String>>()
    var wa: StringBuilder = StringBuilder()

    lines.forEach {
        if (it.isEmpty()) {
            passports.add(createPassport(wa))
            wa = StringBuilder()
        } else {
            wa.append(" ").append(it)
        }
    }
    passports.add(createPassport(wa))

    return passports;
}

private fun createPassport(wa: StringBuilder): MutableMap<String, String> {
    val passport = mutableMapOf<String, String>()
    wa.toString().split(" ").filter(String::isNotEmpty).forEach { keyValString ->
        val split = keyValString.split(":")
        passport[split[0]] = split[1]
    }
    return passport
}
