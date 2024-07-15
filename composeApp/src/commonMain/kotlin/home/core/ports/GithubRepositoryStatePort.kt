package home.core.ports

import home.core.entities.GithubRepository

interface GithubRepositoryStatePort {
    val dataSourcePort: GithubRepositoryDataSourcesPort
    val repository: GithubRepository
    var isFavorite: Boolean
    var progress: Boolean
    var error: Throwable?
}