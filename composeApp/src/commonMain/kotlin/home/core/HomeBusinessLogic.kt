package home.core

import androidx.compose.runtime.Composable
import home.core.ports.HomeStatePort

suspend fun HomeStatePort.fetch() {
    genericError.value = null
    progress.value = true
    runCatching {
        dataSourcePort.getAllRepositories() to dataSourcePort.getAllFavorites()
    }.onSuccess {
        repositories.clear()
        repositories.addAll(it.first)
        favorites.clear()
        favorites.addAll(it.second)
    }.onFailure {
        genericError.value = it
    }
    progress.value = false
}

@Composable
fun HomeStatePort.initialize() {
    val allRepositories = repositories
    val allFavorites = favorites
    selectableRepositories.clear()
    selectableRepositories.addAll(allRepositories.map {
        SelectableGithubRepository(
            isFavorite = allFavorites.any { favorite -> favorite.id == it.id },
            repository = it,
            error = null,
        )
    })
}
