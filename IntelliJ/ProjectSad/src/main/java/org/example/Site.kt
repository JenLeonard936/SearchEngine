package org.example

class Site {
    var title: String? = null;
    var link: String? = null;
    var id: Int = -1;
    var blurb: String? = null;
    var totalScore: Double = 0.0;
    var relevance: Double = 0.0;
    var linksOut = MutableList(0) {Int};
    var incomingLinks = mutableListOf<Int>(1,2,3);
}