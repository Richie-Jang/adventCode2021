package day10

import readInput
import java.util.*

val testCase = """
[({(<(())[]>[[{[]{<()<>>
[(()[<>])]({[<{<<[]>>(
{([(<{}[<>[]}>{[]{[(<()>
(((({<>}<{<{<>}{[]{[]{}
[[<[([]))<([[{}[[()]]]
[{[{({}]{}}([{[{{{}}([]
{<[[]]>}<{[{[{[]{()[[[]
[<(<(<(<{}))><([]([]()
<{([([[(<>()){}]>(<<{{
<{([{{}}[<[[[<>{}]]]>[]]    
""".trimIndent()

val starters = setOf('[', '(', '{', '<')
val enders = setOf(']', ')', '}', '>')

fun getStarter(c: Char): Char {
    return when (c) {
        ']' -> '['
        ')' -> '('
        '>' -> '<'
        '}' -> '{'
        else -> error("not supported")
    }
}

/**
 * @return 0: okay, 1: Incomplete, 2: Corrupted
 */
fun checkLine(s: String): Pair<Int, Char> {

    val stack = Stack<Char>()
    var count = 0
    for (c in s) {
        val isStarter = c in starters
        if (isStarter) {
            stack.add(c)
        } else {
            val sC = getStarter(c)
            val lastOne = stack.peek()
            if (sC == lastOne) {
                // okay
                stack.pop()
            } else {
                //println("Error : $count $c not match prev $sC, current $lastOne")
                return 2 to c
            }
        }
        count++
    }
    return if (stack.size == 0) 0 to ' ' else 1 to ' '
}

fun calculatePart1Point(c: Char): Int {
    return when (c) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        else -> 25137
    }
}

fun getClosingChar (c: Char): Char = when(c) {
    '[' -> ']'
    '(' -> ')'
    '{' -> '}'
    else -> '>'
}
// only handle incomplete
fun checkLine2(s: String): List<Char>? {
    val stack = Stack<Char>()
    for (c in s) {
        val isStarter = c in starters
        if (isStarter) {
            stack.add(c)
        } else {
            val sC = getStarter(c)
            val lastOne = stack.peek()
            if (sC == lastOne) {
                // okay
                stack.pop()
            } else {
                //println("Error : $count $c not match prev $sC, current $lastOne")
                return null
            }
        }
    }
    if (stack.size == 0) return null
    val correctList = arrayListOf<Char>()
    while (stack.size > 0) {
        val p = stack.pop()
        correctList += getClosingChar(p)
    }

    return correctList
}

//part1
fun main1() {
    //val lines = testCase.split('\n').map { it.trim() }
    val lines = readInput("day10")
    val corruptedList = lines.map { checkLine(it) }.filter { it.first == 2}.map { calculatePart1Point(it.second) }
    println(corruptedList.sum())
}

fun computePoint(list: List<Char>): Long {
    fun getPoint(c: Char): Long = when (c) {
        ')' -> 1L
        ']' -> 2L
        '}' -> 3L
        '>' -> 4L
        else -> -1L
    }

    var sum = 0L
    for (l in list) {
        sum = sum * 5L + getPoint(l)
    }
    return sum
}

// part2
fun main() {
    //val lines = testCase.split('\n').map { it.trim() }
    val lines = readInput("day10")
    val list = lines.mapNotNull { checkLine2(it) }.map {
        val v = computePoint(it)
        println(v)
        v
    }.sorted()
    val half = list.size / 2
    println("Error Size: ${list.size}")
    println(list[half])

}