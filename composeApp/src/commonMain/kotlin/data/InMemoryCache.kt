@file:Suppress("UNCHECKED_CAST")

package data

import androidx.annotation.VisibleForTesting
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object InMemoryCache {
    val cache = mutableMapOf<String, Any?>()

    @VisibleForTesting
    fun clear() {
        cache.clear()
    }
}

fun <T> cache(key: String) = object : ReadWriteProperty<Any?, T?> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        println("Cache Operation:")
        println("Fetching $key from cache")
        InMemoryCache.cache.forEach { (key, value) -> println("$key -> $value") }
        val result = InMemoryCache.cache[key]
        println("Found $key: $result")
        println("================")
        return InMemoryCache.cache[key] as? T?
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        println("Cache Operation:")
        println("Updating $key in cache")
        InMemoryCache.cache[key] = value
        InMemoryCache.cache.forEach { (key, value) -> println("$key -> $value") }
        println("================")
    }
}