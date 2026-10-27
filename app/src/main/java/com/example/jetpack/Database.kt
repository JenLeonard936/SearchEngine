package com.example.jetpack

import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.coroutines
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands

@OptIn(ExperimentalLettuceCoroutinesApi::class)
class Database {
    val client: RedisClient = RedisClient.create("redis://localhost:6379")
    val connection: StatefulRedisConnection<String, String> = client.connect()
    val commands: RedisCoroutinesCommands<String, String> = connection.coroutines()

    suspend fun setKeys() {
        commands.set("async_key", "coroutine value")
        val value = commands.get("async_key")
        println("Value: $value")
    }

    suspend fun listKeys() {
        val keys = mutableListOf<String>()
        commands.keys("item:*").collect { keys.add(it) }
        println("Found ${keys.size} item keys")
    }

    /**
     * Reads and returns all values for 'sample_bicycle' in the Redis database.
     * Supports both Redis Hash structures (HGETALL) and String/JSON entries (GET).
     */
    suspend fun getSampleBicycle(): Map<String, String> {
        val key = "sample_bicycle"
        val bicycleData = mutableMapOf<String, String>()

        val keyType = commands.type(key)
        println("Reading '$key' (Redis type: $keyType)")

        when (keyType) {
            "hash" -> {
                commands.hgetall(key).collect { kv ->
                    if (kv.hasValue()) {
                        bicycleData[kv.key] = kv.value
                        println("  ${kv.key}: ${kv.value}")
                    }
                }
            }
            "string" -> {
                val value = commands.get(key)
                if (value != null) {
                    bicycleData["value"] = value
                    println("  value: $value")
                }
            }
            else -> {
                try {
                    commands.hgetall(key).collect { kv ->
                        if (kv.hasValue()) {
                            bicycleData[kv.key] = kv.value
                            println("  ${kv.key}: ${kv.value}")
                        }
                    }
                } catch (_: Exception) {
                    val rawValue = commands.get(key)
                    if (rawValue != null) {
                        bicycleData["value"] = rawValue
                        println("  value: $rawValue")
                    }
                }
            }
        }

        return bicycleData
    }

    fun close() {
        connection.close()
        client.shutdown()
    }
}
