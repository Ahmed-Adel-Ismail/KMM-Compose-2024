package navigation.core.ports

interface NavigationDataSourcePort {
    suspend fun isLoggedIn(): Boolean
}