fun main() {
    fun part1(input: List<String>): Int = input.fold(0) { sum, line ->
        var firstDigit: Int? = null
        var lastDigit: Int? = null

        for (idx in line.indices) {
            if (firstDigit != null && lastDigit != null) {
                break
            }

            val charFromStart = line[idx]
            firstDigit = extractDigit1(firstDigit, charFromStart)
            
            val charFromEnd = line[line.length - idx - 1]
            lastDigit = extractDigit1(lastDigit, charFromEnd)
        }

        (firstDigit ?: 0 ) * DECADE + (lastDigit ?: 0) + sum
    }

    fun part2(input: List<String>): Int = input.fold(0) { sum, line ->
        var firstDigit: Int? = null
        var lastDigit: Int? = null
        var firstDigitStrBuffer = ""
        var lastDigitStrBuffer = ""

        for (idx in line.indices) {
            if (firstDigit != null && lastDigit != null) {
                break
            }

            val charFromStart = line[idx]
            firstDigitStrBuffer += charFromStart
            firstDigit = extractDigit2(firstDigit, charFromStart, firstDigitStrBuffer)

            val charFromEnd = line[line.length - idx - 1]
            lastDigitStrBuffer = charFromEnd + lastDigitStrBuffer
            lastDigit = extractDigit2(lastDigit, charFromEnd, lastDigitStrBuffer)
        }
        firstDigit = extractDigit2(firstDigit, buffer = firstDigitStrBuffer)
        lastDigit = extractDigit2(lastDigit, buffer = lastDigitStrBuffer)

        (firstDigit ?: 0 ) * DECADE + (lastDigit ?: 0) + sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    val testResult = 142
    check(part1(testInput) == testResult)

    val test2Input = readInput("Day01_2_test")
    val test2Result = 281
    check(part2(test2Input) == test2Result)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}

private const val DECADE = 10

private fun extractDigit1(currentDigit: Int?, char: Char): Int? {
    if (currentDigit != null) {
        return currentDigit
    }

    return if (char.isDigit()) char.digitToInt() else null
}

private fun extractDigit2(currentDigit: Int?, char: Char? = null, buffer: String): Int? {
    if (currentDigit != null) {
        return currentDigit
    }
    if (char != null && char.isDigit()) {
        return char.digitToInt()
    }

    return buffer.toDigitOrNull()
}

private fun String.toDigitOrNull(): Int? {
    val matchResult = digitRegexp.find(this) ?: return null
    val (digitStr) = matchResult.destructured

    return when (digitStr) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        else -> null
    }
}

private val digitRegexp = """.*(?<digitStr>one|two|three|four|five|six|seven|eight|nine).*""".toRegex()
