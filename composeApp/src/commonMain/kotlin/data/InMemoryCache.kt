package data

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object InMemoryCache {
    val cache = mutableMapOf<String, Any?>()
}

fun <T> cache(key: String) = object : ReadWriteProperty<Any?, T?> {
    var value: T? = InMemoryCache.cache[key] as? T?
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? = value
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        InMemoryCache.cache[key] = value
    }
}