package day01

import readInput

fun main() {
    fun part1(input: List<String>): Int {
        val data = input.map { it.toInt() }
        val size = data.size
        var counter = 0
<<<<<<< HEAD
        for (i in 0 until size - 1) {
            val a1 = data[i]
            val a2 = data[i + 1]
=======
        for (i in 0 until size-1) {
            val a1 = data[i]
            val a2 = data[i+1]
>>>>>>> origin/main
            if (a1 < a2) {
                counter++
            }
        }
        return counter
    }

    fun part2(input: List<String>): Int {
        val data = input.map { it.toInt() }
        var counter = 0
        val windows3data = data.windowed(3)
        val size = windows3data.size
<<<<<<< HEAD
        for (i in 0 until size - 1) {
            val a1 = windows3data[i].sum()
            val a2 = windows3data[i + 1].sum()
=======
        for (i in 0 until size-1) {
            val a1 = windows3data[i].sum()
            val a2 = windows3data[i+1].sum()
>>>>>>> origin/main
            if (a1 < a2) {
                counter++
            }
        }
        return counter
    }

    fun recursiveWayPart2(input: List<String>): Int {
        val data = input.map { it.toInt() }.windowed(3)
        var counter = 0
        val size = data.size
<<<<<<< HEAD
        fun recRun(index: Int) {
            if (index == size - 1) return
            val a1 = data[index].sum()
            val a2 = data[index + 1].sum()
            if (a1 < a2) {
                counter++
            }
            recRun(index + 1)
=======
        fun recRun (index: Int) {
            if (index == size-1) return
            val a1 = data[index].sum()
            val a2 = data[index+1].sum()
            if (a1 < a2) {
                counter++
            }
            recRun(index+1)
>>>>>>> origin/main
        }

        recRun(0)
        return counter
    }

    val input = readInput("Day01")
<<<<<<< HEAD
    // println(part1(input))
    println(part2(input))
    println(recursiveWayPart2(input))
=======
    //println(part1(input))
    println(part2(input))
    println(recursiveWayPart2(input))

>>>>>>> origin/main
}
