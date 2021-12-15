package day14

import readInput
import java.math.BigInteger

val testCase = """
NNCB

CH -> B
HH -> N
CB -> H
NH -> C
HB -> C
HC -> B
HN -> C
NN -> C
BH -> H
NC -> B
NB -> B
BN -> B
BB -> N
BC -> B
CC -> N
CN -> C    
""".trimIndent()

data class PC(val _1: Char, val _2: Char)

var head: String = ""
var map: Map<PC, Char> = emptyMap()

fun parsingData(ls : List<String>): Pair<String, Map<PC,Char>> {
    var h = ""
    val m = mutableMapOf<PC, Char>()
    var isData = false
    for (l in ls) {
        if (l == "") {
            isData = true
            continue
        }

        when(isData) {
            false -> h = l
            else -> {
                val s = l.split("->").map { it.trim() }
                val pc = PC(s[0][0], s[0][1])
                m[pc] = s[1][0]
            }
        }
    }
    return h to m
}

fun headWindows(h: String): List<PC> {
    return h.windowed(2).map { PC(it[0], it[1]) }
}

fun doStep(hPcs: List<PC>, map: Map<PC, Char>): String {
    return buildString {

        val lastIndex = hPcs.size-1
        var index = 0
        for (pc in hPcs) {
            val c = map.getValue(pc)
            val collect = Triple(pc._1, c, pc._2)
            if (index == lastIndex) {
                append(collect.first).append(collect.second).append(collect.third)
            } else {
                append(collect.first).append(collect.second)
            }
            index++
        }
    }

}

// part1
fun main1() {

    val lines = readInput("day14")
    //val (head, map) = parsingData( testCase.split('\n').map{it.trim()} )
    val (head, map) = parsingData( lines )
    var nline = head

    repeat(10) {
        val heads = headWindows(nline)
        nline = doStep(heads,map)
        //println("Step[${it+1}] : $nline")
    }

    val countMap = mutableMapOf<Char, BigInteger>()
    for (c in nline) {
        val ct = countMap.getOrDefault(c, BigInteger.ZERO)
        countMap[c] = ct + BigInteger.ONE
    }

    val cclist = countMap.toList().sortedBy { it.second }
    val small = cclist.first().second
    val big = cclist.last().second

    println(big-small)

}

// part2
fun main() {
    //val (head, map) = parsingData( testCase.split('\n').map{it.trim()} )
    val (head, map) = parsingData(readInput("day14"))

    val heads: List<PC> = headWindows(head)
    var cmap = hashMapOf<PC, Long>()

    for (h in heads) {
        cmap[h] = cmap.getOrDefault(h, 0L) + 1L
    }

    repeat(40) {
        val nmap = hashMapOf<PC, Long>()
        for (p in cmap) {
            val pc = p.key
            val c = map.getValue(pc)
            val left = PC(pc._1, c)
            val right = PC(c, pc._2)
            nmap[left] = nmap.getOrDefault(left, 0) + p.value
            nmap[right] = nmap.getOrDefault(right, 0) + p.value
        }
        cmap = nmap
    }

    val ks = cmap.keys.toList().flatMap { listOf(it._1, it._2) }.distinct()

    val resMap = hashMapOf<Char, Long>()
    for (k in ks) {
        var sum = 0L
        for (g in cmap) {
            if (g.key._1 == k) {
                sum += g.value
            }
        }
        resMap[k] = sum
    }

    println(resMap)

    fun printResult(m : Map<Char, Long>) {
        val list = m.toList().sortedBy { it.second }
        val res = list.last().second - list.first().second + 1
        println(res)
    }

    printResult(resMap)

}