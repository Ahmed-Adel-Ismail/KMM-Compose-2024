package home.core.ports

import home.core.entities.GithubRepository

interface GithubRepositoryDataSourcesPort {
    suspend fun addToFavorites(repository: GithubRepository)
    suspend fun removeFromFavorites(repository: GithubRepository)
}