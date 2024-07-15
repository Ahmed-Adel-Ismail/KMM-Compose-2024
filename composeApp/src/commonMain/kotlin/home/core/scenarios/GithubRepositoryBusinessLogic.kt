package home.core.scenarios

import home.core.entities.GithubRepository
import home.core.ports.GithubRepositoryStatePort


suspend fun GithubRepositoryStatePort.initialize(favorites: List<GithubRepository>): GithubRepositoryStatePort {
    error = null
    progress = true
    isFavorite = favorites.any { it.id == repository.id }
    progress = false
    return this
}

suspend fun GithubRepositoryStatePort.addToFavorites() {
    if (progress) return
    error = null
    progress = true
    runCatching { dataSourcePort.addToFavorites(repository) }
        .onSuccess { isFavorite = true }
        .onFailure { error = it }
    progress = false
}

suspend fun GithubRepositoryStatePort.removeFromFavorites() {
    if (progress) return
    error = null
    progress = true
    runCatching { dataSourcePort.removeFromFavorites(repository) }
        .onSuccess { isFavorite = false }
        .onFailure { error = it }
    progress = false
}

