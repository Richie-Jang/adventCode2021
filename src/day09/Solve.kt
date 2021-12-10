package day09

import readInput

val testCase = """
2199943210
3987894921
9856789892
8767896789
9899965678
""".trimIndent()

fun convert2DArray(lines: List<String>): Array<IntArray> {
    return lines.map {
        it.map { Character.digit(it, 10) }.toIntArray()
    }.toTypedArray()
}

data class Pos(val x: Int, val y: Int)

val accessDists = listOf(
    -1 to 0,
    1 to 0,
    0 to -1,
    0 to 1
)
fun getAdjIndices(x: Int, y: Int, xLen: Int, yLen: Int): List<Pos> {
    return accessDists.asSequence().mapNotNull { (xx, yy) ->
        val nx = x + xx
        val ny = y + yy
        if (nx < 0 || nx >= xLen || ny < 0 || ny >= yLen) null else Pos(nx, ny)
    }.toList()
}

fun main1() {

    // part1 : 577
    //val lines = testCase.split('\n').map { it.trim() }
    val lines = readInput("day09")
    val grid = convert2DArray(lines)

    val yLen = grid.size
    val xLen = grid[0].size

    val store = arrayListOf<Int>()

    for (y in grid.indices) {
        for (x in grid[0].indices) {
            val v = grid[y][x]
            val adjs = getAdjIndices(x, y, xLen, yLen).map { grid[it.y][it.x] }
            if (adjs.all { it > v }) {
                store += v
            }
        }
    }

    // testCase
    val result = store.map { it+1 }.sum()
    println(result)
}


fun basinCheckingCount(grid: Array<IntArray>, lx: Int, ly: Int, store: HashSet<Pos>, visited: HashSet<Pos>) {
    val curPos = Pos(lx, ly)
    store.add(curPos)
    visited.add(curPos)
    val curValue = grid[ly][lx]
    val adjs = getAdjIndices(lx, ly, grid[0].size, grid.size).filter {
        it !in visited && grid[it.y][it.x] <= 8
    }
    for (p in adjs) {
        val vv = grid[p.y][p.x]
        if (curValue < vv) {
            basinCheckingCount(grid, p.x, p.y, store, visited)
        }
    }
}

// part2
fun main() {
    val lines = readInput("day09")
    //val lines = testCase.split('\n').map { it.trim() }
    val grid = convert2DArray(lines)

    val xLen = grid[0].size
    val yLen = grid.size

    val store = arrayListOf<Pos>()  // lowest

    for (y in 0 until yLen) {
        for (x in 0 until xLen) {
            val v = grid[y][x]
            val adjPoses = getAdjIndices(x, y, xLen, yLen)
            val adjValues = adjPoses.map { grid[it.y][it.x] }
            val checkingPoint = adjValues.all { it > v }
            if (checkingPoint) {
                store += Pos(x,y)
            }
        }
    }

    fun printResult(st: Set<Pos>) {
        val newGrid =
            grid.withIndex()
                .map {
                    val y = it.index
                    it.value.withIndex().map {
                        val x = it.index
                        val v = it.value
                        val isOn = st.contains(Pos(x,y))
                        v to isOn
                    }
                }
        for (y in newGrid.indices) {
            val vs = newGrid[y]
            val str = vs.joinToString("", transform = {a ->
                if (a.second) a.first.toString() else "-"
            })
            println(str)
        }
        println()
    }

    // part checking
    val result2 =
        store.map {
            val st = HashSet<Pos>()
            val visited = HashSet<Pos>()
            basinCheckingCount(grid, it.x, it.y, st, visited)
            //printResult(st)
            st.size
        }.sortedDescending()

    val answer = result2.slice(0..2).fold(1) { acc,t -> t * acc }
    println(answer)

}