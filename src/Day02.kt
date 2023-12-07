import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int = input.fold(0) { sum, line ->
        sum + if (isPossibleGame(line)) line.parseGameId() else 0
    }

    fun part2(input: List<String>): Int = input.fold(0) { sum, line ->
        sum + getPower(line)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    val testResult = 8
    val result = part1(testInput)
    check(result == testResult)

    val test2Result = 2286
    val result2 = part2(testInput)
    check(result2 == test2Result)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun String.parseGameId(): Int {
    val gameNumberSubStr = this.substringBefore(":")

    val matchResult = gameIdRegex.find(gameNumberSubStr) ?: return 0
    val (gameIdStr) = matchResult.destructured

    return gameIdStr.toIntOrNull() ?: 0
}

private val gameIdRegex = """Game (?<gameIdStr>\d+).*""".toRegex()

private fun String.parseColorSet(): Cubes {
    val matchResult = cubesRegexp.find(this.trim()) ?: return Cubes.empty()
    val (numberStr, color) = matchResult.destructured

    val number = numberStr.toIntOrNull() ?: 0
    return when (color) {
        "red" -> Cubes(reds = number)
        "green" -> Cubes(greens = number)
        "blue" -> Cubes(blues = number)
        else -> Cubes.empty()
    }
}

private val cubesRegexp = """^(?<numberStr>\d+) (?<color>red|green|blue).*""".toRegex()

private data class Cubes(
    val reds: Int = 0,
    val blues: Int = 0,
    val greens: Int = 0
) {
    companion object {
        fun empty() = Cubes(0, 0, 0)

        fun combineToMax(first: Cubes, second: Cubes) = Cubes(
            reds = max(first.reds, second.reds),
            blues = max(first.blues, second.blues),
            greens = max(first.greens, second.greens)
        )

    }
    
    val power: Int
        get() = reds * blues * greens

    operator fun plus(increment: Cubes): Cubes = Cubes(
        reds = this.reds + increment.reds,
        blues = this.blues + increment.blues,
        greens = this.greens + increment.greens
    )

    fun isEnoughForSet(setState: Cubes): Boolean =
        this.reds >= setState.reds && this.blues >= setState.blues && this.greens >= setState.greens
}

private val cubesInBag = Cubes(
    reds = 12,
    greens = 13,
    blues = 14
)

private fun isPossibleGame(line: String): Boolean =
    line.substringAfter(":").split(";").fold(true) { isEnoughForGame, setStr ->
        if (!isEnoughForGame) return@fold false

        val setState = setStr.split(",").fold(Cubes.empty()) { roundState, setColorStr ->
            roundState + setColorStr.parseColorSet()
        }

        return@fold cubesInBag.isEnoughForSet(setState)
    }

private fun getPower(line: String): Int =
    line.substringAfter(":").split(";")
        .fold(Cubes.empty()) { minimalCubes, setStr ->
            
            val setCubes = setStr.split(",").fold(Cubes.empty()) { roundState, setColorStr ->
                roundState + setColorStr.parseColorSet()
            }

            return@fold Cubes.combineToMax(minimalCubes, setCubes)
        }.power