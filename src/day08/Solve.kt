package day08

import readInput

fun search1478(data: List<String>): Int {
    val checkSet = setOf(2, 3, 4, 7)
    return data.count {
        val s = it.toSet().size
        s in checkSet
    }
}

fun mainPart1() {
    val inputs = readInput("day08")
    var part1Sum = 0
    for (i in inputs) {
        val s = i.split('|')
        val s2 = s[1].split(' ')
        part1Sum += search1478(s2)
    }
    println("Part1: $part1Sum")
}

val singleMap = mapOf(
    2 to 1,  //single
    4 to 4,  // single
    3 to 7,  // single
    7 to 8  // single
)
val singleCases = setOf(2, 4, 3, 7)

fun countChars(s: String): Int = s.toSet().size

typealias StrPair = Pair<String, Set<Char>>

fun getInputsOnLine(s: String): Pair<List<StrPair>, List<StrPair>> {
    val ss = s.split('|').map { it.trim() }
    return ss[0].split(' ').map { it to it.toSet() } to ss[1].split(' ').map { it to it.toSet() }
}


// part2

fun updateSingleCases(inputs: List<StrPair>, posMap: MutableMap<String, Set<Char>>) {
    val numMap = mutableMapOf<Int, StrPair>()
    inputs.forEach {
        val count = it.second.size
        if (count in singleMap) {
            val num = singleMap[count]!!
            numMap[num] = it
        }
    }
    // cross check
    // 7-1 : top
    // 8-7 : left1, left2, middle, bottom
    // 4-1 : left1, middle
    // 8-4-top : left2, bottom
    // 1 : right1, right2
    val c_7_1 = numMap[7]!!.second - numMap[1]!!.second
    posMap["top"] = c_7_1
    posMap["left1-middle"] = numMap[4]!!.second - numMap[1]!!.second
    posMap["left2-bottom"] = numMap[8]!!.second - numMap[4]!!.second - posMap["top"]!!
    posMap["right1-right2"] = numMap[1]!!.second
}

fun update5Case(inputs: List<StrPair>, posMap: MutableMap<String, Set<Char>>) {
    // check
    // 2&3&5 = top, middle, bottom
    fun checking(cds: List<StrPair>) {
        val ccc = cds.map { it.second }.distinct()
        val c1 = ccc[0].intersect(ccc[1]).intersect(ccc[2]) - posMap["top"]!! // middle, bottom

        posMap["middle"] = c1.intersect(posMap["left1-middle"]!!)
        posMap["bottom"] = c1.intersect(posMap["left2-bottom"]!!)
        posMap["left1"] = posMap["left1-middle"]!! - posMap["middle"]!!
        posMap["left2"] = posMap["left2-bottom"]!! - posMap["bottom"]!!
    }

    val candidates =
        inputs.filter {
            val count = it.second.size
            count == 5 && it.second.intersect(posMap["top"]!!) == posMap["top"]!!
        }
    checking(candidates)
}

fun update6Case(inputs: List<StrPair>, posMap: MutableMap<String, Set<Char>>) {
    // handle right1, right2
    // 0,6,9
    val cds = inputs.filter {
        val count = it.second.size
        count == 6
    }.map { it.second }.distinct()

    val r1_r2 = posMap["right1-right2"]!!
    val f2 = cds.find {
        it.intersect(r1_r2).size == 1
    }!!.intersect(r1_r2)

    posMap["right2"] = f2
    posMap["right1"] = r1_r2 - f2
}

val searcherMap = mapOf(
    1 to "left1",
    2 to "right1",
    3 to "left2",
    4 to "right2",
    5 to "top",
    6 to "middle",
    7 to "bottom"
)

val c0 = setOf(1,2,3,4,5,7)
val c1 = setOf(2,4)
val c2 = setOf(2,3,5,6,7)
val c3 = setOf(2,4,5,6,7)
val c4 = setOf(1,2,4,6)
val c5 = setOf(1,4,5,6,7)
val c6 = setOf(1,3,4,5,6,7)
val c7 = setOf(2,4,5)
val c8 = setOf(1,2,3,4,5,6,7)
val c9 = setOf(1,2,4,5,6,7)

fun main() {
    // part2
    //val inputs = listOf("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf")

    // test_case
    val test_case = """
        be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
        edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
        fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
        fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
        aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
        fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
        dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
        bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
        egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
        gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
    """.trimIndent()

    val inputs = readInput("day08")
    //val inputs = test_case.split('\n').map { it.trim() }

    var sum = 0

    val segments = listOf(c0,c1,c2,c3,c4,c5,c6,c7,c8,c9).map { it.map { searcherMap[it]!!} }

    for (line in inputs) {
        val posMap = mutableMapOf<String, Set<Char>>()
        val (first,second) = getInputsOnLine(line)
        val totalInputs = (first + second)

        updateSingleCases(totalInputs, posMap)
        update5Case(totalInputs, posMap)
        update6Case(totalInputs, posMap)

        val second_chars = second.map { it.second }
        val segment_chars = segments.withIndex()
            .map {
                val index = it.index
                val data = it.value
                data.map { posMap[it]!!.first() }.toSet() to index
            }.toMap()

        val result =
            second_chars.map {
                segment_chars[it]!!
            }.joinToString("").toInt()

        sum += result
    }

    println(sum)

}