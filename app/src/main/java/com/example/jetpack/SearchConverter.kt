package com.example.jetpack

import java.io.File

class SearchConverter {
    fun wordSplitter(word: String): List<String> {
        return word.replace("\\p{Punct}".toRegex(), "").lowercase().split(" ")
    }

    fun removeStopwords(passage: List<String>): List<String> {
        val stopwords = try {
            File("java/org/example/stopwords").readLines()
        } catch (e: Exception) {
            listOf(
                "a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
                "any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being",
                "below", "between", "both", "but", "by", "can't", "cannot", "could", "couldn't",
                "did", "didn't", "do", "does", "doesn't", "doing", "don't", "down", "during",
                "each", "few", "for", "from", "further", "had", "hadn't", "has", "hasn't",
                "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here",
                "here's", "hers", "herself", "him", "himself", "his", "how", "how's", "i",
                "i'd", "i'll", "i'm", "i've", "if", "in", "into", "is", "isn't", "it", "it's",
                "its", "itself", "let's", "me", "more", "most", "mustn't", "my", "myself",
                "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought",
                "our", "ours", "ourselves", "out", "over", "own", "same", "shan't", "she",
                "she'd", "she'll", "she's", "should", "shouldn't", "so", "some", "such", "than",
                "that", "that's", "the", "their", "theirs", "them", "themselves", "then",
                "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've",
                "this", "those", "through", "to", "too", "under", "until", "up", "very", "was",
                "wasn't", "we", "we'd", "we'll", "we're", "we've", "were", "weren't", "what",
                "what's", "when", "when's", "where", "where's", "which", "while", "who",
                "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you",
                "you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves"
            )
        }
        val newPassage = mutableListOf<String>()
        for (word in passage) {
            if (!stopwords.contains(word.lowercase())) {
                newPassage.add(word)
            }
        }
        return newPassage.toList()
    }

    /**
     * Stems a single word by stripping common English suffixes (e.g. -sses, -ies, -ing, -ed, -s, -ly, -ness, -ment).
     */
    fun stem(word: String): String {
        val w = word.lowercase().trim()
        if (w.length <= 2) return w

        var result = w

        fun containsVowel(str: String): Boolean = str.any { it in "aeiou" }

        // Plurals and suffix rules
        when {
            result.endsWith("sses") -> result = result.dropLast(2) // caresses -> caress
            result.endsWith("ies") -> result = result.dropLast(2)  // ponies -> poni
            result.endsWith("ss") -> { /* keep ss */ }
            result.endsWith("s") && !result.endsWith("us") && !result.endsWith("is") -> {
                result = result.dropLast(1) // cats -> cat
            }
        }

        if (result.endsWith("eed")) {
            val stemPart = result.dropLast(3)
            if (containsVowel(stemPart)) {
                result = result.dropLast(1) // agreed -> agree
            }
        } else if (result.endsWith("ed")) {
            val stemPart = result.dropLast(2)
            if (containsVowel(stemPart)) {
                result = step1bPostProcess(stemPart)
            }
        } else if (result.endsWith("ing")) {
            val stemPart = result.dropLast(3)
            if (containsVowel(stemPart)) {
                result = step1bPostProcess(stemPart)
            }
        }

        if (result.endsWith("y") && result.length > 2) {
            val stemPart = result.dropLast(1)
            if (containsVowel(stemPart)) {
                result = stemPart + "i" // happy -> happi
            }
        }

        return removeCommonSuffixes(result, ::containsVowel)
    }

    /**
     * Stems a list of words.
     */
    fun stem(words: List<String>): List<String> {
        return words.map { stem(it) }
    }

    private fun step1bPostProcess(stem: String): String {
        return when {
            stem.endsWith("at") || stem.endsWith("bl") || stem.endsWith("iz") -> stem + "e"
            stem.length >= 2 && stem.last() == stem[stem.length - 2] && stem.last() !in listOf(
                'l',
                's',
                'z'
            ) -> stem.dropLast(1)

            else -> stem
        }
    }

    fun findEditDistance(word: String, match: String): Int {
        var i = 0
        var j = 0
        var lev =
            MutableList<MutableList<Int>>(word.length) { MutableList<Int>(match.length) { 0 } }
        var temp = mutableListOf(0, 0, 0)
        while (i < word.length) {
            while (j < match.length) {
                if (i == 0) {
                    lev[i][j] = j
                } else if (j == 0) {
                    lev[i][j] = i
                } else {
                    temp[0] = lev[i - 1][j] + 1
                    temp[1] = lev[i][j - 1] + 1
                    temp[2] = lev[i - 1][j - 1]
                    if (i == j) {
                        temp[2]++
                    }
                    lev[i][j] = temp.min()
                }
                j++
            }
            i++
        }
        return lev[word.length - 1][match.length - 1]
    }

    private fun removeCommonSuffixes(word: String, containsVowel: (String) -> Boolean): String {
        val suffixes = listOf(
            "ational" to "ate",
            "tional" to "tion",
            "ization" to "ize",
            "isation" to "ize",
            "fulness" to "ful",
            "ousness" to "ous",
            "iveness" to "ive",
            "ment" to "",
            "ness" to "",
            "able" to "",
            "ible" to "",
            "ally" to "al",
            "fully" to "ful",
            "lessly" to "less",
            "ly" to "",
            "ful" to ""
        )

        for ((suffix, replacement) in suffixes) {
            if (word.endsWith(suffix)) {
                val stemPart = word.dropLast(suffix.length)
                if (stemPart.length >= 2 && containsVowel(stemPart)) {
                    return stemPart + replacement
                }
            }
            return word
        }
        return word
    }

//    fun bkTree(root: Node, words: List<String>) {
//        val i = root.word.length
//        var temp = 0
//        root.children.fill(Node())
//        for (word in words) {
//            temp = findEditDistance(root.word, word)
//            if (root.children[temp].isEmpty()) {
//                root.children[temp] = Node(word, MutableList<Node>(word.length) {})
//            } else {
//                bkTree(root.children[temp], listOf(word))
//            }
//        }
//    }
}
