package navigation.core.ports

interface NavigationDataSourcePort {
    suspend fun isLoggedIn(): Boolean
    suspend fun setLoggedIn(value: Boolean)
}