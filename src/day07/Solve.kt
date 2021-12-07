package day07

import readInput
import kotlin.math.abs


val testInput = "16,1,2,0,4,2,7,1,2,14"

fun computeAverage(list: List<Int>): Double = list.average()

fun computeMedianAround(list : List<Int>): List<Int> {
    val sorted = list.sorted()
    val half = sorted.size / 2
    val med = sorted[half]
    var decreasing = half - 1
    var smallMed = -1
    while (decreasing > 0) {
        if (med != sorted[decreasing]) {
            smallMed = sorted[decreasing]
            break
        }
        decreasing--
    }
    var bigMed = -1
    var increasing = half + 1
    while(increasing < sorted.size) {
        if (med != sorted[increasing]) {
            bigMed = sorted[increasing]
            break
        }
        increasing++
    }
    if (smallMed != -1 && bigMed != -1) return listOf(smallMed, med, bigMed)
    else error("small / big median can not find it")
}


val countMap = mutableMapOf<Int, Int>()

fun computeUsageFuel(point: Int, list: List<Int>): Int {

    fun incremental(count: Int): Int {
        if (count == 0) {
            return 0
        }
        if (count !in countMap) {
            val rr = incremental(count-1) + count
            countMap[count] = rr
            return rr
        } else {
            return countMap.getValue(count)
        }
    }

    var sum = 0
    for (l in list) {
        sum += incremental(abs(l - point))
    }
    return sum
}

fun main() {

    //val inputs = testInput.split(',').map { it.toInt() }
    val inputs = readInput("day07")[0].split(',').map { it.toInt() }
    println(inputs.size)

    val min = inputs.minOrNull()!!
    val max = inputs.maxOrNull()!!

    println("Min: $min, Max: $max")

    val a1 = computeMedianAround(inputs)
    println(a1)

    // setup countMap to 100, for fast calculation
    var s = 0
    countMap[0] = 0
    for (i in 1 .. 100) {
        countMap[i] = i + countMap.getValue(i-1)
    }


    var answer = Int.MAX_VALUE
    (min .. max).forEach {
        val fuel = computeUsageFuel(it, inputs)
        if (fuel < answer) answer = fuel
    }

    println("Answer : $answer")
}