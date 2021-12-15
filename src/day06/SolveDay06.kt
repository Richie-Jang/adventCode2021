package day06

import readInput

val testCase = "3,4,3,1,2"

fun runCycle(fishes: ArrayList<Int>, day: Int) {
    val newOnes = arrayListOf<Int>()
    for (index in fishes.indices) {
        val f = fishes[index]
        if (f == 0) {
            newOnes += 8
            fishes[index] = 6
        } else {
            fishes[index]--
        }
    }
    if (newOnes.isNotEmpty()) {
        fishes.addAll(newOnes)
    }

    //println("day $day: ${fishes.joinToString(",")}")
}

fun main1() {

    val fishes = arrayListOf<Int>()
    // udpate
    val inputs = readInput("day06").first()
    inputs.split(',').map{it.toInt()}.forEach { fishes += it }

    for (i in 1 .. 80) {
        runCycle(fishes, i)
    }

    println(fishes.size)
}

class RefLongArray(var value: LongArray)

fun runCyclePart2(map: RefLongArray, day: Int) {
    val temp = LongArray(10)
    for(i in 8 downTo 1) {
        val fs = map.value[i]
        temp[i-1] = fs
    }
    // 0
    val zeroV = map.value[0]
    temp[6] += zeroV
    temp[8] = zeroV
    map.value = temp
}

// part2
fun main() {
    val map = RefLongArray(LongArray(10))
    //
    //testCase.split(',').map {
    readInput("day06").first().split(',').map {
        val c = it.toInt()
        map.value[c]++
    }

    repeat(256) {
        runCyclePart2(map, it+1)
    }

    // sum
    println(map.value.sum())

}