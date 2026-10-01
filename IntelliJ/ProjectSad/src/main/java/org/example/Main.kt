import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import kotlin.time.Duration.Companion.seconds

// Singleton object holding a single OkHttpClient instance.
// This client is reused for all HTTP requests in the app.
object HttpClient {
    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            // Max time allowed to establish a TCP connection
            .connectTimeout(10.seconds)
            // Max time allowed to read the response body
            .readTimeout(20.seconds)
            .build()
    }
}

// Pagination example for Books to Scrape.
// The scraper loads a page, extracts book titles,
// finds the "next" page link, and repeats.

fun main() {
//    val startUrl = "https://books.toscrape.com/"
//    var currentUrl = startUrl
//
//    // Limit how many pages we scrape in this demo
//    val maxPages = 3
//    var currentPage = 1
//
//    while (currentPage <= maxPages) {
//        println("Scraping page $currentPage: $currentUrl")
//
//        // Build an HTTP GET request for the current page
//        val request = Request.Builder()
//            .url(currentUrl)
//            // User-Agent header to look like a real browser
//            .header(
//                "User-Agent",
//                "Mozilla/5.0 (compatible; Kotlin web scraping pagination example)"
//            )
//            .build()
//
//        try {
//            // Execute the request using the shared HTTP client
//            HttpClient.client.newCall(request).execute().use { response ->
//
//                // Stop if the server returned a non-OK status
//                if (!response.isSuccessful) {
//                    println("Request failed with status: ${response.code}")
//                    return
//                }
//
//                // Read the HTML response body as a string
//                val html = response.body?.string()
//                if (html.isNullOrBlank()) {
//                    println("Empty response body, stopping.")
//                    return
//                }
//
//                // Parse HTML using the *current page URL* as base
//                // This allows Jsoup to resolve relative links correctly
//                val doc = Jsoup.parse(html, currentUrl)
//
//                // Select all book cards on the page
//                val bookCards = doc.select("ol.row > li")
//                println("Found ${bookCards.size} books on this page.")
//
//                // Extract and print each book title
//                bookCards.forEach { card ->
//                    val title = card
//                        .selectFirst("article.product_pod h3 a")
//                        ?.attr("title")
//                        ?.trim()
//                        ?: "Unknown title"
//
//                    println(" - $title")
//                }
//
//                // Locate the "next" page link in the pagination section
//                val nextLinkEl = doc.selectFirst("ul.pager li.next a")
//                if (nextLinkEl == null) {
//                    println("No next page found. Done.")
//                    return
//                }
//
//                // Convert the relative "href" into an absolute URL
//                val nextUrl = nextLinkEl.absUrl("href")
//                if (nextUrl.isBlank()) {
//                    println("Next link exists but could not be resolved. Done.")
//                    return
//                }
//
//                // Update state for the next loop iteration
//                currentUrl = nextUrl
//                currentPage++
//            }
//
//            // Small pause between requests to avoid hitting the site too fast
//            Thread.sleep(1500)
//
//        } catch (e: Exception) {
//            println("Pagination scrape failed: ${e.message}")
//            return
//        }
//    }
//
//    println("Finished pagination example.")

    val arr = listOf(1, 2, 3)
    println(arr[1])
}