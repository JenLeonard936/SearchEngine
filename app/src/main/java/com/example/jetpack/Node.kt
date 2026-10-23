package com.example.jetpack

data class TrieNode(
    var children: MutableMap<Char, TrieNode> = mutableMapOf(),
    var isEnd: Boolean = false
)

data class Trie(
    var root: TrieNode = TrieNode()
) {
    fun insert(word: String) {
        var node = root
        for (char in word) {
            node = node.children.getOrPut(char) { TrieNode() }
        }
        node.isEnd = true
    }
}

data class Node(
    val word: String = "",
    val children: MutableList<Node> = mutableListOf()
)
