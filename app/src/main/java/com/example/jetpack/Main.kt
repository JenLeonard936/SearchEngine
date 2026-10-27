package com.example.jetpack

fun main() {
    val s = SearchConverter()
    val arr = listOf("hello", "i", "am", "a", "stopword", "converter", "and", "im", "cool")
    println(s.removeStopwords(arr))
}
