package com.example.projectsad

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import org.jsoup.Jsoup

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
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
        }
    }
}