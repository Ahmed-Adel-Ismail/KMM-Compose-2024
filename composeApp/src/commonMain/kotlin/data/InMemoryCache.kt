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

fun <T> cache(key: String, onChanged: ((T?) -> Unit)? = null) =
    object : ReadWriteProperty<Any?, T?> {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            return InMemoryCache.cache[key] as? T?
        }

        override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            InMemoryCache.cache[key] = value
            onChanged?.invoke(value)
        }
    }