package home.core.scenarios

import home.core.entities.GithubRepository
import home.core.ports.GithubRepositoryStatePort
import home.core.ports.HomeStatePort

suspend fun HomeStatePort.initialize() {
    error = null
    progress = true
}

suspend fun HomeStatePort.fetchAllRepositories() =
    runCatching { dataSourcePort.getAllRepositories() }.onFailure { error = it }
        .getOrDefault(listOf())


suspend fun HomeStatePort.fetchAllFavorites() =
    runCatching { dataSourcePort.getAllFavorites() }.onFailure { error = it }
        .getOrDefault(listOf())


suspend fun HomeStatePort.merge(
    fetchedRepositories: List<GithubRepository>,
    favorites: List<GithubRepository>,
    repositoryPortFactory: (GithubRepository) -> GithubRepositoryStatePort
) {
    if (fetchedRepositories.isNotEmpty()) {
        repositories.addAll(fetchedRepositories.map { repositoryPortFactory(it).initialize(favorites) })
    }
    progress = false
}

