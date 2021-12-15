package day11

import readInput

val testCase = """
5483143223
2745854711
5264556173
6141336146
6357385478
4167524645
2176841721
6882881134
4846848554
5283751526    
""".trimIndent()

val testCase1 = """
6988888888
9988888888
8888888888
8888888888
8888888888
8888888888
8888888888
8888888888
8888888888
8888888888 
""".trimIndent()

val dirs = listOf(
    -1 to 0,
    1 to 0,
    0 to -1,
    0 to 1,
    -1 to -1,
    1 to -1,
    -1 to 1,
    1 to 1
)

fun flashingAdjs(y: Int, x: Int, grid: Array<IntArray>,
                 readys: Set<Pair<Int,Int>>): List<Pair<Int,Int>>{

    return dirs.map { it.first + y to it.second + x }
        .filter { it.first in grid.indices && it.second in grid[0].indices && it !in readys }
}

fun passTime(grid: Array<IntArray>, time: Int): Int {

    var flashCount = 0

    val yLen = grid.size
    val xLen = grid[0].size
    // increasing 1
    for (y in 0 until yLen) {
        for (x in 0 until xLen) {
            grid[y][x]++
        }
    }

    fun checkingOver9(): List<Pair<Int,Int>> {
        val flashingList = mutableListOf<Pair<Int,Int>>()
        for (y in 0 until yLen) {
            for (x in 0 until xLen) {
                val curValue = grid[y][x]
                if (curValue > 9) {
                    flashingList.add(y to x)
                }
            }
        }
        return flashingList
    }

    // checking
    var flashingList = checkingOver9()
    val readyFlashes = HashSet<Pair<Int,Int>>()

    while (flashingList.isNotEmpty()) {
        flashingList.forEach { readyFlashes.add(it) }
        val adjs = flashingList.flatMap { flashingAdjs(it.first, it.second, grid, readyFlashes) }
        // update
        adjs.forEach { grid[it.first][it.second]++ }
        readyFlashes.forEach {
            grid[it.first][it.second] = 0
        }
        flashingList = checkingOver9()
    }

    flashCount += readyFlashes.size

    /*if (time % 10 == 0) {
        println("Step: $time")
        printGrid(grid)
    }*/
    return flashCount
}

fun printGrid (grid: Array<IntArray>) {
    grid.forEach {
        println(it.joinToString(""))
    }
}

fun main() {
    // testcase
    /*val grid: Array<IntArray> =
        testCase1.split('\n').map { it.trim().toCharArray().map { Character.digit(it, 10)}.toIntArray() }.toTypedArray()*/

    val grid = readInput("day11")
        .map {
            it.trim().toCharArray().map {
                Character.digit(it, 10)
            }.toIntArray()
        }.toTypedArray()

    val allCount = grid.sumOf { it.size }

    // part1
    var result = 0

    while(true) {
        result++
        if (allCount == passTime(grid, result)) {
            break
        }
    }

    println(result)
}