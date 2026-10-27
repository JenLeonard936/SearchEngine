//package com.example.jetpack
//
//import kotlinx.serialization.json.Json
//import redis.clients.jedis.JedisPooled
//import redis.clients.jedis.params.ScanParams
//
//data class Site2(
//    var title: String? = null,
//var link: String? = null,
//var id: Int = -1,
//var blurb: String? = null,
//var totalScore: Double = 0.0,
//var relevance: Double = 0.0,
//var linksOut: MutableList<Int> = mutableListOf(),
//var incomingLinks: MutableList<Int> = mutableListOf(),
//){
//    fun fetchUsersByPrefix(
//        host: String = "localhost",
//        port: Int = 6379,
//        prefix: String = "user:*"
//    ): List<Site2> {
//        val jedis = JedisPooled(host, port)
//        val keys = mutableListOf<String>()
//
//        // 1. Safely scan for all keys matching the class prefix
//        var cursor = ScanParams.SCAN_POINTER_START
//        val scanParams = ScanParams().match(prefix).count(100)
//
//        do {
//            val scanResult = jedis.scan(cursor, scanParams)
//            keys.addAll(scanResult.result)
//            cursor = scanResult.cursor
//        } while (cursor != ScanParams.SCAN_POINTER_START)
//
//        if (keys.isEmpty()) {
//            jedis.close()
//            return emptyList<Site2>()
//        }
//
//        // 2. Fetch all matching JSON strings at once and deserialize into a List
//        val jsonValues: List<String?> = jedis.mget(*keys.toTypedArray())
//        jedis.close()
//
//        return jsonValues.filterNotNull().map { jsonString ->
//            Json.decodeFromString<User>(jsonString)
//        }
//    }
//
//}
