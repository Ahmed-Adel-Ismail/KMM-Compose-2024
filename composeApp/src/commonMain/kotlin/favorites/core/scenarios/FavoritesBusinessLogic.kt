package favorites.core.scenarios

import favorites.core.ports.FavoriteStatePort

suspend fun FavoriteStatePort.removeFromFavorite() {
    progress = true
    runCatching { dataSources.removeFromFavorites(favoriteRepository) }
        .onFailure { error = it }
    progress = false
}