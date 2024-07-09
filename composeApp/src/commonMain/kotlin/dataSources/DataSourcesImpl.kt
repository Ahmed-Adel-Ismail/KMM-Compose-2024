package dataSources

import kotlinx.coroutines.delay

/**
 * add expect when integrating with real data sources, if we had to
 * do platform specific logic
 *
 * as this is a sample project, all integration points are grouped in one file for now
 * for the sake of simplicity
 */
object DataSourcesImpl : DataSources {

    private var token by cache<String>("dataSources.expected.token")

    override suspend fun getUsernameValidation(): List<(String?) -> Boolean> {
        delay(1000) // simulate server delay
        return listOf(
            { it != null && it.length > 4 },
            { it?.contains("@") == true },
        )
    }

    override suspend fun getPasswordValidations(): List<(String?) -> Boolean> {
        delay(1000) // simulate server delay
        return listOf(
            { it != null && it.length > 4 }
        )
    }

    override suspend fun postLogin(username: String?, password: String?): String {
        delay(3000) //simulate server delay
        return "$username|$password"
    }

    override suspend fun saveToken(token: String) {
        this.token = token
    }

    /**
     * in a real application we have to handle token refresh and expiration
     */
    override suspend fun isLoggedIn(): Boolean {
        return token != null
    }
}