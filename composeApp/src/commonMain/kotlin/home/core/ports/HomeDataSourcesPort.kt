package home.core.ports

import home.core.GithubRepository

interface HomeDataSourcesPort {
    suspend fun getAllRepositories(): List<GithubRepository>
}