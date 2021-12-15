package day15

import readInput
import java.util.*

val testCase = """
1163751742
1381373672
2136511328
3694931569
7463417111
1319128137
1359912421
3125421639
1293138521
2311944581    
""".trimIndent()

typealias  Pos = Pair<Int,Int> // x, y

val nexts = listOf(
    -1 to 0,
    1 to 0,
    0 to -1,
    0 to 1
)

fun getNextPos(p: Pos, xLen: Int, yLen: Int): List<Pos> {
    return nexts.map { Pos(p.first + it.first, p.second + it.second) }
        .filter { it.first in 0 until xLen && it.second in 0 until yLen }
}

// part1
fun main1() {

    //val cavern = testCase.split('\n').map{it.trim().map { Character.digit(it, 10)}}
    val cavern = readInput("day15").map { it.map { Character.digit(it, 10) } }
    val distMap = mutableMapOf<Pos,Int>().apply {
        for (y in cavern.indices) {
            for (x in cavern[0].indices) {
                val pos = Pos(x,y)
                if (pos == Pos(0,0)) {
                    this[pos] = 0
                } else {
                    this[pos] = Int.MAX_VALUE
                }
            }
        }
    }

    val xLen = cavern[0].size
    val yLen = cavern.size

    println("XLen: $xLen, YLen: $yLen")

    val comp = kotlin.Comparator<Pair<Pos,Int>> { o1, o2 ->
        o1.second.compareTo(o2.second)
    }

    val queue = PriorityQueue(comp)
    val visited = mutableSetOf<Pos>()
    queue.add(Pos(0,0) to 0)

    while (queue.isNotEmpty()) {
        val (p,d) = queue.poll()
        visited.add(p)
        val ns = getNextPos(p, xLen, yLen).filter { it !in visited }
        for (n in ns) {
            // update distMap
            val cur = cavern[n.second][n.first] + d
            if (distMap[n]!! > cur) {
                distMap[n] = cur
                queue.add(n to cur)
            }
        }
    }

    println(distMap[Pos(xLen-1, yLen-1)])
}

// part2

val cavernRanges = (0 .. 4).map {
    val x = (100 * it) to (100 * it + 99)
    val y = (100 * it) to (100 * it + 99)
    x to y
}

fun getOrgPosAndIndexXY(pos: Pos, xLen: Int, yLen: Int): Pair<Pos, Pair<Int,Int>> {
    val xIndex = pos.first / xLen
    val yIndex = pos.second / yLen
    val xmod = pos.first % xLen
    val ymod = pos.second % yLen
    return Pair(Pos(xmod, ymod), Pair(xIndex, yIndex))
}

fun getRiskValue(cavern: List<List<Int>>, xyIndex: Pair<Int,Int>, orgPos: Pos): Int {
    var v = cavern[orgPos.second][orgPos.first]
    for (y in 1 .. xyIndex.second) {
        v += 1
    }
    if (v >= 10) {
        v -= 9
    }
    for (x in 1 .. xyIndex.first) {
        v += 1
    }
    if (v >= 10) v -= 9
    return v
}

fun main() {
    //val cavern = testCase.split('\n').map{it.trim().map { Character.digit(it, 10)}}
    val cavern = readInput("day15").map { it.map { Character.digit(it, 10) } }
    val (yLen, xLen) = run {
        cavern.size to cavern[0].size
    }
    // xLen : 100, yLen : 100

    val xTotalLen = xLen * 5
    val yTotalLen = yLen * 5

    val endPos = Pos(xTotalLen-1, yTotalLen-1)
    val distMap = mutableMapOf<Pos, Int>()
    val visited = mutableSetOf<Pos>()
    val comp = kotlin.Comparator<Pair<Pos,Int>> { o1, o2 ->  o1.second.compareTo(o2.second) }
    val pq = PriorityQueue(comp)
    val startPos = Pos(0,0)
    distMap[startPos] = 0
    pq.add(startPos to 0)

    while (pq.isNotEmpty()) {
        val (p, d) = pq.poll()
        visited.add(p)

        val nextPoss = getNextPos(p, xTotalLen, yTotalLen).filter { it !in visited }
        for (np in nextPoss) {
            val vdist = distMap.getOrPut(np) { Int.MAX_VALUE }
            val (pos, indexer) = getOrgPosAndIndexXY(np, xLen, yLen)
            val v = getRiskValue(cavern, indexer, pos)
            val cur = v + d
            if (vdist > cur) {
                distMap[np] = cur
                pq.add(np to cur)
            }
        }
    }

    println(distMap[endPos]!!)

}