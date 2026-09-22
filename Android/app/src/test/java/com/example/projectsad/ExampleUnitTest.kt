package com.example.projectsad

import org.junit.Test
import org.jsoup.Jsoup

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest{

    @Test
    val html = """
        <li class="col-xs-6 col-sm-4 col-md-3 col-lg-3">
          <article class="product_pod">
            <h3>
              <a href="catalogue/a-light-in-the-attic_1000/index.html" title="A Light in the Attic">
                A Light in the ...
              </a>
            </h3>
          </article>
        </li>
    """.trimIndent()
    try {
        val doc = Jsoup.parse(html)
        val firstTitle = doc.selectFirst("article.product_pod h3 a")
            ?.attr("title")
            ?: "Missing title"

        println(firstTitle)
    } catch (e: Exception) {
        println("Failed to parse HTML: ${e.message}")
}}