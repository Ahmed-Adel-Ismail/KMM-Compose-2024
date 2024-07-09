package dataSources

/**
 * an interface to be implemented in unit tests and by different actual classes if required
 *
 * all methods are optional to make unit testing easier
 */
interface DataSources {
    suspend fun isLoggedIn(): Boolean = false
    suspend fun getUsernameValidation(): List<(String?) -> Boolean> = emptyList()
    suspend fun getPasswordValidations(): List<(String?) -> Boolean> = emptyList()
    suspend fun postLogin(username: String?, password: String?) : String = ""
    suspend fun saveToken(token: String) = Unit
}

