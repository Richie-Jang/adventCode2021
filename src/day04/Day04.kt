package day04

import readInput
import readTestInput

data class Item(var v: Int, var selected: Boolean = false)
typealias Grid = Array<Array<Item>>  // 5 x 5

fun setup(lines: List<String>): Pair<List<Int>, List<Grid>> {
    val header = lines.take(1)[0].split(',').map{it.toInt()}
    val res = arrayListOf<Grid>()
    var lineCount = 0
    for (l in lines.drop(1).filter{ it != "" }) {
        if (lineCount == 0) {
            res.add(Array(5) { Array(5) { Item(0)} })
        }
        val cur = res.last()
        val ss = l.split(' ').filter{ it != "" }.map { it.toInt() }

        ss.indices.forEach {
            cur[lineCount][it].v = ss[it]
        }
        if (lineCount == 4) lineCount = 0 else lineCount++
    }

    return header to res
}

fun markGrid(grid: Grid, callValue: Int) {
    fFor@ for (i in 0 until 5) {
        for (j in 0 until 5) {
            if (grid[i][j].v == callValue) {
                grid[i][j].selected = true
                break@fFor
            }
        }
    }
}

fun checkBingo(grid: Grid): Boolean {
    // hor
    for (i in 0 until 5) {
        val r = grid[i].all { it.selected }
        if (r) return true
    }

    // ver
    for (i in 0 until 5) {
        val gg = (0 until 5).map { grid[it][i] }
        val r = gg.all { it.selected }
        if (r) return true
    }

    return false
}

fun collectSumFromGrid(grid: Grid): Int {
    var sum = 0
    for (y in 0 until 5) {
        for (x in 0 until 5) {
            if (!grid[y][x].selected) {
                sum += grid[y][x].v
            }
        }
    }
    return sum
}

fun printGrid(g: Grid) {
    for(i in g.indices) {
        for (j in g[0].indices) {
            print(g[i][j].v)
            print(' ')
        }
        println()
    }
}

// part1
fun main1() {

    val inputs = readTestInput("day04")
    val (l, grids) = setup(inputs)

    (0 until 4).forEach { c ->
        grids.forEach { markGrid(it, l[c]) }
    }

    var foundGrid: Grid? = null
    var lastCall = -1

    forF@ for (i in (4 until l.size)) {
        for (gi in grids.indices) {
            val g = grids[gi]
            markGrid(g, l[i])
            if (checkBingo(g)) {
                foundGrid = g
                lastCall = l[i]
                break@forF
            }
        }
    }

    if (foundGrid != null) {
        //answer
        val v1 = collectSumFromGrid(foundGrid)
        println("Answer : ${v1 * lastCall}")
    } else {
        // not found ?? never happened for this
        println("Never Happened")
    }

}

// part2
fun main() {
    val input = readInput("day04")
    val (callInts, grids) = setup(input)
    // first 0 ~ 4 call number
    (0 until 4).forEach { c ->
        grids.forEach { markGrid(it, callInts[c]) }
    }

    val doneGridSet = mutableMapOf<Grid, Int>()
    var lastDoneGrid : Grid? = null

    for (i in (4 until callInts.size)) {
        // check doneGridSet has all
        if (doneGridSet.size == grids.size) break

        for (gi in grids.indices) {
            val g = grids[gi]
            if (g in doneGridSet) continue
            val callNum = callInts[i]
            markGrid(g, callNum)
            if (checkBingo(g)) {
                doneGridSet[g] = callNum
                lastDoneGrid = g
            }
        }
    }

    val v1 = collectSumFromGrid(lastDoneGrid!!)
    val v2 = doneGridSet.getValue(lastDoneGrid!!)
    println(v1 * v2)

}