package data

import GithubRepositoryData
import OwnerData
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import data.models.AllGithubRepositoriesData
import kotlinx.coroutines.delay

/**
 * add expect when integrating with real data sources, if we had to
 * do platform specific logic
 *
 * as this is a sample project, all integration points are grouped in one file for now
 * for the sake of simplicity
 */
// TODO: data to be fetched from actual datasource
object DataSourcesImpl : DataSources {

    private var token by cache<String>("data.token")
    private var favorites by cache<List<GithubRepositoryData>>("data.favorites") {
        favoritesSize.value = it?.size ?: 0
    }
    private val favoritesSize: MutableState<Int> = mutableStateOf(favorites?.size ?: 0)

    override suspend fun getUsernameValidation(): List<(String?) -> Boolean> {
        delay(1000) // simulate server delay
        return listOf(
            { it != null && it.length > 4 },
            { it?.contains("@") == true },
        )
    }

    override suspend fun getPasswordValidations(): List<(String?) -> Boolean> {
        delay(1000) // simulate server delay
        return listOf({ it != null && it.length > 4 })
    }

    override suspend fun postLogin(username: String?, password: String?): String {
        delay(2000) //simulate server delay
        return "$username|$password"
    }

    override suspend fun saveToken(token: String) {
        this.token = token
    }

    /**
     * in a real application we have to handle token refresh and expiration
     */
    override suspend fun isLoggedIn(): Boolean {
        delay(1000) // simulate IO delay
        return token != null
    }

    override suspend fun getAllGithubRepositories(): AllGithubRepositoriesData {
        // return Json.decodeFromString(GithubRepositoriesMockResponse)
        delay(3000) //simulate server delay
        return AllGithubRepositoriesData(data = (1..500).map { mockGithubRepository(it) })
    }

    override suspend fun addToFavorites(repository: GithubRepositoryData) {
        delay(1000) // simulate IO delay
        favorites = favorites.orEmpty() + repository
    }

    override suspend fun removeFromFavorites(repository: GithubRepositoryData) {
        delay(1000) // simulate IO delay
        favorites = favorites.orEmpty() - repository
    }

    override suspend fun getAllFavorites(): List<GithubRepositoryData> {
        delay(1000) // simulate IO delay
        return favorites.orEmpty()
    }

    private fun mockGithubRepository(id: Int = 1) = GithubRepositoryData(
        id = id.toLong(),
        name = "Github Repository $id",
        isPrivate = false,
        description = "Github Repository $id description section",
        stargazersCount = id * 1000,
        languagesUrl = "https://api.github.com/repos/octocat/boysenberry-repo-1/languages",
        owner = OwnerData(
            id = (id * 3000).toLong(),
            isPrivate = false,
            name = "Author $id",
            avatarUrl = "https://picsum.photos/${ratio()}/${ratio()}"
        )
    )

    private fun ratio(): Int = (150..300).random()

    override fun observeOnFavoritesSize(): State<Int> {
        return favoritesSize
    }
}