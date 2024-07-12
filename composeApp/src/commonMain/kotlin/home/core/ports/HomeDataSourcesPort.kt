package home.core.ports

import home.core.GithubRepository

interface HomeDataSourcesPort {
    suspend fun getAllRepositories(): List<GithubRepository>
    suspend fun getAllFavorites(): List<GithubRepository>
    suspend fun addToFavorites(repository: GithubRepository)
    suspend fun removeFromFavorites(repository: GithubRepository)
}