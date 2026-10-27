package com.example.jetpack

data class Site(
    var id: Int = -1,
    var title: String = "",
    var url: String = "",
    var description: String = "",
    var totalScore: Double = 0.0,
    var relevance: Double = 0.0,
    var linksOut: MutableList<Int> = mutableListOf(),
    var incomingLinks: MutableList<Int> = mutableListOf()
) {
    // Aliases for compatibility with legacy field names
    var link: String
        get() = url
        set(value) { url = value }

    var blurb: String
        get() = description
        set(value) { description = value }
}
