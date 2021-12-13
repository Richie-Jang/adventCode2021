package day13

import readInput
import kotlin.math.max

val testCase = """
6,10
0,14
9,10
0,3
10,4
4,11
6,0
6,12
4,1
0,13
10,12
3,4
3,0
8,4
1,10
2,14
8,10
9,0

fold along y=7
fold along x=5""".trimIndent()

data class Pos(val x: Int, val y: Int)
data class Fold(val isX: Boolean, val value: Int)

fun getInputs(lines: List<String>): Pair<List<Pos>, List<Fold>> {
    val res1 = arrayListOf<Pos>()
    val res2 = arrayListOf<Fold>()
    val regFold = """([xy])=(\d+)""".toRegex()
    var isFoldData = false

    for (l in lines) {
        if (l == "") {
            isFoldData = true
            continue
        }

        when (isFoldData) {
            false -> {
                val s = l.split(',').map { it.toInt() }
                res1 += Pos(s[0], s[1])
            }
            else -> {
                val mat = regFold.find(l) ?: error("$l can not parse")
                val f1 = mat.groupValues[1]
                val f2 = mat.groupValues[2].toInt()
                val isX = f1 == "x"
                res2 += Fold(isX, f2)
            }
        }
    }
    return res1 to res2
}

/**
 * get max x and y values
 */
fun getMaxXAndY(poss: List<Pos>): Pair<Int,Int> {
    var maxx = 0
    var maxy = 0
    for (p in poss) {
        if (p.x > maxx) maxx = p.x
        if (p.y > maxy) maxy = p.y
    }
    return maxx to maxy
}

fun updateGrid(grid: Array<IntArray>, poss: List<Pos>) {
    for (p in poss) {
        grid[p.y][p.x] = 1
    }
}

fun printGrid(grid: Array<IntArray>) {
    grid.forEach {
        println(it.joinToString("", transform = { v -> if (v == 1) "#" else "."}))
    }
}

fun handleNonFoldOneY(orgs: Array<IntArray>, news: Array<IntArray>, b1: List<Int>, b2: List<Int>) {
    val bRange = if (b1.size >= b2.size) b1 else {
        val d = b2.size - b1.size
        val st = d - (b1.size)
        (st .. (st + b1.size - 1)).toList()
    }

    for (y in bRange) {
        val org = orgs[y]
        for (x in org.indices) {
            if (news[y][x] == 1) continue
            news[y][x] = org[x]
        }
    }
}

fun handleNonFoldOneX(orgs: Array<IntArray>, news: Array<IntArray>, b1: List<Int>, b2: List<Int>) {
    val bRange = if (b1.size >= b2.size) b1 else {
        val d = b2.size - b1.size
        val st = d - (b1.size)
        (st .. (st + b1.size - 1)).toList()
    }

    for (y in orgs.indices) {
        val org = orgs[y]
        for (x in bRange) {
            if (news[y][x] == 1) continue
            news[y][x] = org[x]
        }
    }
}

/**
 * @return folding is larger
 */
fun handleFoldOneY(orgs: Array<IntArray>, news: Array<IntArray>, sRange: List<Int>, fold: Int) {
    val rev = sRange.reversed()
    val size = sRange.size
    val upRange = run {
        val g = fold - size
        if (g >= 0) {
            (g .. (g + size - 1)).toList()
        } else {
            (0 .. size-1).toList()
        }
    }
    val pairs = rev.zip(upRange)
    for ((b,t) in pairs) {
        val org = orgs[b]
        for (x in org.indices) {
            news[t][x] = org[x]
        }
    }
}


fun handleFoldOneX(orgs: Array<IntArray>, news: Array<IntArray>, sRange: List<Int>, fold: Int) {
    val rev = sRange.reversed()
    val size = sRange.size
    val upRange = run {
        val g = fold - size
        if (g >= 0) {
            (g .. (g + size - 1)).toList()
        } else {
            (0 .. size-1).toList()
        }
    }
    val pairs = rev.zip(upRange)

    for (y in orgs.indices) {
        val org = orgs[y]
        for ((b,t) in pairs) {
            news[y][t] = org[b]
        }
    }
}

fun foldX(grid : Array<IntArray>, v: Int) : Array<IntArray> {
    val uplen1 = (0 until v).toList()
    val uplen2 = (v+1 until grid[0].size).toList()
    val len = max(uplen1.size, uplen2.size)
    val ngrid = Array(grid.size) { IntArray (len) }

    // fold
    handleFoldOneX(grid, ngrid, uplen2, v)
    handleNonFoldOneX(grid, ngrid, uplen1, uplen2)

    return ngrid
}

fun foldY(grid: Array<IntArray>, v: Int): Array<IntArray> {
    val uplen1 = (0 until v).toList()
    val uplen2 = (v+1 until grid.size).toList()
    val len = max(uplen1.size, uplen2.size)
    val ngrid = Array(len) { IntArray (grid[0].size) }

    // fold
    handleFoldOneY(grid, ngrid, uplen2, v)
    handleNonFoldOneY(grid, ngrid, uplen1, uplen2)

    return ngrid
}

fun main() {

    // val lines = testCase.split('\n').map{ it.trim() }
    val lines = readInput("day13")

    val (poss, folds) = getInputs(lines)

    val (maxx, maxy) = getMaxXAndY(poss)
    var grid = Array(maxy+1) { IntArray(maxx+1) }

    // update grid
    updateGrid(grid, poss)

    // part2
    for (p in folds) {
        if (p.isX) {
            grid = foldX(grid, p.value)
        } else {
            grid = foldY(grid, p.value)
        }
        //printGrid(grid)
    }

    // part
    /*val firstFold = folds.first()
    if (firstFold.isX) {
        grid = foldX(grid, firstFold.value)
    } else {
        grid = foldY(grid, firstFold.value)
    }*/


    // count
    var sum = 0
    for (y in grid.indices) {
        for (x in grid[0].indices) {
            if (grid[y][x] == 1) sum += 1
        }
    }

    printGrid(grid)

    println(sum)
}