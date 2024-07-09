package data

import data.models.AllGithubRepositoriesData

/**
 * an interface to be implemented in unit tests and by different actual classes if required
 *
 * all methods are optional to make unit testing easier
 */
interface DataSources {
    suspend fun isLoggedIn(): Boolean = throw NotImplementedError()
    suspend fun getUsernameValidation(): List<(String?) -> Boolean> = throw NotImplementedError()
    suspend fun getPasswordValidations(): List<(String?) -> Boolean> = throw NotImplementedError()
    suspend fun postLogin(username: String?, password: String?): String =
        throw NotImplementedError()

    suspend fun saveToken(token: String): Unit = throw NotImplementedError()
    suspend fun getAllGithubRepositories(): AllGithubRepositoriesData = throw NotImplementedError()
}
