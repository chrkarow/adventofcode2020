import java.io.File

fun main() {
    val lines =
        File("/Users/Chrischi/Desktop/advent-of-code/src/main/resources/input6.txt").readLines()

    val result = createAnswerPairs(lines).map { countAnswers(it.first, it.second) }.sum()
    println(result)
}

fun countAnswers(answers: List<Char>, numberOfPeople: Int): Int {
    return answers.groupingBy { it }.eachCount().values.filter{it == numberOfPeople}.count()
}

fun createAnswerPairs(lines: List<String>): List<Pair<List<Char>, Int>> {

    val answerPairs = mutableListOf<Pair<List<Char>, Int>>()
    var wa: StringBuilder = StringBuilder()
    var peopleInGroup = 0

    lines.forEach {
        if (it.isEmpty()) {
            answerPairs.add(
                Pair(
                    wa.toList(),
                    peopleInGroup
                )
            )
            wa = StringBuilder()
            peopleInGroup = 0
        } else {
            peopleInGroup += 1
            wa.append(it)
        }
    }
    answerPairs.add(
        Pair(
            wa.toList(),
            peopleInGroup
        )
    )

    return answerPairs;
}
