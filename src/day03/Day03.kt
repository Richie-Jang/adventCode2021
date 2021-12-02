package day03

import readInput

val testLines = """
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
""".trimIndent()

fun updateArrForOneBit(s: String, arr: IntArray) {
    for (i in s.indices) {
        when (s[i]) {
            '1' -> arr[i]++
        }
    }
}

fun main1() {

    val arr = (1 .. 12).map { 0 }.toIntArray()
    val lines = readInput("day03")
    val count = lines.size

    lines.forEach {
        updateArrForOneBit(it, arr)
    }

    // compute rate
    val rateBitStr = (1 .. 12).map { '0' }.toCharArray()
    val epsilonBitStr = (1 .. 12).map {'1'}.toCharArray()

    for (i in arr.indices) {
        val c = arr[i]
        val nonc = count - c
        if (c > nonc) {
            rateBitStr[i] = '1'
            epsilonBitStr[i] = '0'
        }
    }

    val rate = String(rateBitStr).toInt(2)
    val epsilon = String(epsilonBitStr).toInt(2)

    val day1Result = rate * epsilon

    println(day1Result)


}

fun searchRate(lines: List<String>, index: Int, isOxygen: Boolean): String {
    if (lines.size == 1) {
        return lines[0]
    }

    val obArr = arrayListOf<String>()
    val zbArr = arrayListOf<String>()

    for (l in lines) {
        if (l[index] == '1') {
            obArr += l
        } else {
            zbArr += l
        }
    }

    val oxygenCount = obArr.size
    val co2Count = zbArr.size
    // select
    when (isOxygen) {
        true -> {
            if (oxygenCount >= co2Count) {
                return searchRate(obArr, index+1, isOxygen)
            } else {
                return searchRate(zbArr, index+1, isOxygen)
            }
        }
        else -> {
            if (oxygenCount >= co2Count) {
                return searchRate(zbArr, index+1, isOxygen)
            } else {
                return searchRate(obArr, index+1, isOxygen)
            }
        }
    }
}


// part2
fun main() {
    val lines = readInput("day03")
    //val lines = testLines.split("\n").map { it.trim() }

    val oxy = searchRate(lines, 0, true)
    val co2 = searchRate(lines, 0, false)

    val a1 = oxy.toInt(2)
    val b1 = co2.toInt(2)
    println(a1)
    println(b1)
    val result = a1 * b1
    println(result)
}