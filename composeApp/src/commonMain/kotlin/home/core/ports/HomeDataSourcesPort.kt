package home.core.ports

import home.core.entities.GithubRepository

interface HomeDataSourcesPort {
    suspend fun getAllRepositories(): List<GithubRepository>
    suspend fun getAllFavorites(): List<GithubRepository>
}