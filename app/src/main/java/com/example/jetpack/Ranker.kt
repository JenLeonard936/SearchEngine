package com.example.jetpack

import java.util.PriorityQueue

class Ranker {
    var sites = mutableListOf<Site>()

    fun mergeSort(webs: MutableList<Site>, left: Int, right: Int) {
        if (left < right) {
            val middle = left + (right - left) / 2
            mergeSort(webs, left, middle)
            mergeSort(webs, middle + 1, right)
            merge(webs, left, middle, right)
        }
    }

    fun merge(webs: MutableList<Site>, left: Int, middle: Int, right: Int) {
        val p1 = middle - left + 1
        val p2 = right - middle

        val one = mutableListOf<Site>()
        val two = mutableListOf<Site>()

        var i = 0
        while (i < p1) {
            one.add(webs[left + i])
            i++
        }

        i = 0
        while (i < p2) {
            two.add(webs[middle + 1 + i])
            i++
        }

        var a = 0
        var b = 0
        var k = left

        while (a < p1 && b < p2) {
            if (one[a].totalScore >= two[b].totalScore) {
                webs[k] = one[a]
                a++
            } else {
                webs[k] = two[b]
                b++
            }
            k++
        }

        while (a < p1) {
            webs[k] = one[a]
            a++
            k++
        }
        while (b < p2) {
            webs[k] = two[b]
            b++
            k++
        }
    }

    fun findWords(targets: List<String>, text: List<String>): List<MutableList<Int>> {
        val positions = List(targets.size) { mutableListOf<Int>() }
        for (i in targets.indices) {
            for (t in text.indices) {
                if (targets[i].equals(text[t], ignoreCase = true)) {
                    positions[i].add(t)
                }
            }
        }
        return positions
    }

    fun findDistance(positions: List<MutableList<Int>>): Int {
        if (positions.isEmpty() || positions.any { it.isEmpty() }) return Int.MAX_VALUE
        var minScore = Int.MAX_VALUE
        for (p in positions[0]) {
            // Distance calculation
        }
        return minScore
    }

    fun fixDeadEnds(sites: List<Site>) {
        for (site in sites) {
            if (site.linksOut.isEmpty()) {
                for (i in sites.indices) {
                    site.linksOut.add(i)
                }
            }
        }
    }

    fun findRelevance(sites: List<Site>) {
        if (sites.isEmpty()) return
        for (site in sites) {
            site.relevance = 1.0 / sites.size
        }

        val newRel = MutableList(sites.size) { 0.0 }
        val d = 0.85

        for (iteration in 0..100) {
            for (site in sites) {
                var tempSum = 0.0
                for (i in site.incomingLinks) {
                    if (i in sites.indices && sites[i].linksOut.isNotEmpty()) {
                        tempSum += sites[i].relevance / sites[i].linksOut.size
                    }
                }
                newRel[site.id] = (1 - d) + (d * tempSum)
            }

            for (site in sites) {
                if (site.id in newRel.indices) {
                    site.relevance = newRel[site.id]
                }
            }
        }
    }

    data class Occurrence(val index: Int, val listIndex: Int, val elementIndex: Int)

    fun calculatePhraseScore(queryTerms: List<List<String>>, articleWords: List<String>): Double {
        if (queryTerms.isEmpty() || articleWords.isEmpty()) return 0.0

        val normalizedArticle = articleWords.map { it.lowercase() }

        val indicesPerTerm = queryTerms.map { termGroup ->
            val groupLower = termGroup.map { it.lowercase() }
            val indices = mutableListOf<Int>()
            normalizedArticle.forEachIndexed { index, word ->
                if (word in groupLower) {
                    indices.add(index)
                }
            }
            indices
        }

        if (indicesPerTerm.any { it.isEmpty() }) {
            return 0.0
        }

        val minHeap = PriorityQueue<Occurrence>(compareBy { it.index })
        var currentMax = -1

        for (i in indicesPerTerm.indices) {
            val firstIdx = indicesPerTerm[i][0]
            minHeap.add(Occurrence(firstIdx, i, 0))
            currentMax = maxOf(currentMax, firstIdx)
        }

        var minSpan = Int.MAX_VALUE

        while (minHeap.isNotEmpty()) {
            val current = minHeap.poll() ?: break
            val currentSpan = currentMax - current.index
            minSpan = minOf(minSpan, currentSpan)

            val listIndex = current.listIndex
            val nextElementIndex = current.elementIndex + 1

            if (nextElementIndex >= indicesPerTerm[listIndex].size) {
                break
            }

            val nextIdx = indicesPerTerm[listIndex][nextElementIndex]
            minHeap.add(Occurrence(nextIdx, listIndex, nextElementIndex))
            currentMax = maxOf(currentMax, nextIdx)
        }

        return 100.0 / (1.0 + minSpan.toDouble())
    }
}
