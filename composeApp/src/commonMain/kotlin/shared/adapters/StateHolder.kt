package shared.adapters

import data.cache

class StateHolder<T>(val name: String, private val initialState: T) : Clearable {
    private var state by cache<T?>(name)

    fun state(): T = state ?: initialState.also { state = it }

    override fun clear() {
        val finalState = state
        if (finalState is Clearable) finalState.clear()
        state = null
    }
}