package data

import GithubRepositoryData
import OwnerData
import data.models.AllGithubRepositoriesData
import data.models.UserData
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

    private var userData by cache<UserData>("data.userData")
    private var favorites by cache<List<GithubRepositoryData>>("data.favorites")

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

    override suspend fun postLogin(username: String?, password: String?): UserData {
        delay(2000) //simulate server delay
        return UserData(id = 0, token = "$username|$password")
    }

    override suspend fun saveUser(userData: UserData) {
        this.userData = userData
    }

    /**
     * in a real application we have to handle token refresh and expiration
     */
    override suspend fun isLoggedIn(): Boolean {
        delay(1000) // simulate IO delay
        return userData != null
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
}