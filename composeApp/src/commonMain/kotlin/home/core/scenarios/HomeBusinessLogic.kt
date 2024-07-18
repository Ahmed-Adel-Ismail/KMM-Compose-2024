package home.core.scenarios

import home.core.entities.GithubRepository
import home.core.ports.GithubRepositoryStatePort
import home.core.ports.HomeStatePort
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

suspend fun HomeStatePort.listAll(
    ioDispatcher: CoroutineDispatcher,
    repositoryPortFactory: (GithubRepository) -> GithubRepositoryStatePort
) = coroutineScope {
    error = null
    progress = true
    val repositories = async(ioDispatcher) { fetchAllRepositories() }
    val favorites = async(ioDispatcher) { fetchAllFavorites() }
    merge(repositories.await(), favorites.await(), repositoryPortFactory)
}

private suspend fun HomeStatePort.fetchAllRepositories() =
    runCatching { dataSourcePort.getAllRepositories() }
        .onFailure { error = it }
        .getOrDefault(listOf())


private suspend fun HomeStatePort.fetchAllFavorites() =
    runCatching { dataSourcePort.getAllFavorites() }
        .onFailure { error = it }
        .getOrDefault(listOf())


private suspend fun HomeStatePort.merge(
    fetchedRepositories: List<GithubRepository>,
    favorites: List<GithubRepository>,
    repositoryPortFactory: (GithubRepository) -> GithubRepositoryStatePort
) {
    if (fetchedRepositories.isNotEmpty()) {
        repositories.addAll(fetchedRepositories.map { repositoryPortFactory(it).initialize(favorites) })
    }
    progress = false
}

