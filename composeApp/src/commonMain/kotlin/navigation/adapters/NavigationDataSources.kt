package navigation.adapters

import dataSources.cache
import kotlinx.coroutines.delay
import navigation.core.ports.NavigationDataSourcePort

class NavigationDataSources(
    isLoggedInKey: String = "navigation.adapters.isLoggedIn"
) : NavigationDataSourcePort {
    private var isLoggedIn by cache<Boolean?>(isLoggedInKey)

    override suspend fun isLoggedIn(): Boolean {
        delay(3000) // simulate server delay
        return isLoggedIn == true
    }

    override suspend fun setLoggedIn(value: Boolean) {
        delay(3000) // simulate server delay
        isLoggedIn = value
    }
}