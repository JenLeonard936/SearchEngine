package org.example

class Ranker {
    var sites = mutableListOf<Site>()

    fun mergeSort(webs: MutableList<Site>, left: Int, right: Int){
        if(left > right){
            var middle = left + (right - 1) / 2
            mergeSort(webs, left, middle)
            mergeSort(webs, middle + 1, right)
            merge(webs, left, middle, right)
        }
    }
    fun merge(webs: MutableList<Site>, left: Int, middle: Int, right: Int,){
        var p1 = middle - left + 1;
        var p2 = right - middle;

        var one = mutableListOf<Site>()
        var two = mutableListOf<Site>()

        var i = 0;
        while(i < p1){
            one[i] = webs[i + left]
            i++
        }

        i = 0
        while(i < p2){
            two[i] = webs[middle + i + 1]
        }


        var a = 0
        var b = 0
        var k = left;

        while(a < p1 && b < p2){
            if(one[a].totalScore < two[b].totalScore){
                webs[k] = one[a]
                a++;
            }
            else{
                webs[k] = two[b]
                b++
            }
            k++
        }

        while(a < p1) {
            webs[k] = one[a]
            a++
            k++
        }
        while(b < p2){
            webs[k] = two[b]
            b++
            k++
        }
    }

    fun findWords(targets: List<String>, text: List<String>): List<MutableList<Int>>{
        var t = 0
        var i = 0
        var positions = List(targets.size) { mutableListOf<Int>() }
        while(i < targets.size){
            while(t < text.size){
                if(targets[i].equals(text[t])){
                    positions[i].add(t)
                }
                t++
            }
            if(positions[i].isEmpty()){
                t = 0
            }
            else{
                t = positions[i][0]
            }
            i++;
        }
        return positions
    }

    fun findDistance(positions: List<MutableList<Int>>){
        // Ranks combinations of distances by how far apart they are
        // Score penalty if one of the words is missing
        // Returns lowest score possible
    }

    // Need to have method that turns links into nums equal to list index
    // Also need to add every outgoing link to incoming link var in corresponding page
    fun findRelevance(sites: List<Site>){
        for(site in sites) {
            site.relevance = 1.0 / sites.size
        }
        var newRel = List(sites.size){Double}
        var d = 0.12    // dumping factor
        var tempSum: Double
        while(true){
            for(site in sites){
                tempSum = 0.0
                for(i in site.incomingLinks){
                    tempSum += sites[i].relevance/sites[i].linksOut.size
                }
                newRel[site.id] = (1 - d) + d*tempSum
            }
            for(site in sites){
                site.relevance = newRel[site.id]
            }
        }
    }
}