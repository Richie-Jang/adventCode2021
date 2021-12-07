package day06

import readInput

val test_input = "3,4,3,1,2"

data class LanternFish(var timer: Int)

// day check and timer adjustment
fun checkingDay(oldFishes: MutableList<LanternFish>) {
    val newCandidates = mutableListOf<LanternFish>()
    oldFishes.forEach {
        it.timer--
        if (it.timer == -1) {
            it.timer = 6
            newCandidates += LanternFish(8)
        }
    }

    oldFishes += newCandidates
}

// part1
fun main1() {

    val oldFishList = mutableListOf<LanternFish>()

    // update oldfishList
    test_input.split(',').sorted().forEach{ oldFishList += LanternFish(it.toInt()) }

    /*readInput("day06")[0].split(',').forEach {
        oldFishList += LanternFish(it.toInt())
    }*/

    val printFishes = {
        val pp = oldFishList.sortedBy { it.timer }

        println("%05d ".format(oldFishList.size-5)+pp.joinToString(",", transform = {it.timer.toString()}))
    }

    printFishes()
    repeat(35) {
        checkingDay(oldFishList)
        if ((it+1) % 7 == 0) {
            printFishes()
        }
    }

    val fishCount = oldFishList.size
    println(fishCount)

}

fun count1Day(arr: LongArray): LongArray {
    val res = LongArray(9)

    for (i in 8 downTo 1) {
        val v = arr[i]
        res[i-1] += v
    }

    val i0 = arr[0]
    res[6] += i0
    res[8] += i0
    return res
}

fun main() {

    var arr = LongArray(9)

    /*test_input.split(',').forEach {
        val i = it.toInt()
        arr[i]++
    }*/

    readInput("day06")[0].split(',').forEach {
        arr[it.toInt()]++
    }

    fun updateArrForDay7() {
        val i7 = arr[7]
        val i8 = arr[8]
        fun computeNew(v: Int) = (v + 2) % 9

        val newArr = LongArray(9)

        for (i in 0 .. 6) {
            val v = arr[i]
            newArr[i] += v
            val nv = computeNew(i)
            if (v > 0) newArr[nv] += v
        }

        if (i7 > 0) newArr[computeNew(7)] += i7
        if (i8 > 0) newArr[computeNew(8)] += i8

        arr = newArr
    }

    // part1 80days
    repeat(36) {
        updateArrForDay7()
    }

    println("passing 1")

    repeat(4) {
        arr = count1Day(arr)
    }

    println(arr.sum())
}