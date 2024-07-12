package shared.adapters

import data.cache

class StateHolder<T>(val name: String, private val initialState: T) {
    private var state by cache<T?>(name)

    fun state(): T = state ?: initialState.also { state = it }

    fun clear() {
        state = null
    }
}