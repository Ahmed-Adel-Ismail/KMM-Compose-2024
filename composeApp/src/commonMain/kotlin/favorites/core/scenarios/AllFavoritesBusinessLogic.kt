package favorites.core.scenarios

import favorites.core.entities.FavoriteRepository
import favorites.core.ports.AllFavoritesStatePort
import favorites.core.ports.FavoriteStatePort


suspend fun AllFavoritesStatePort.initialize(
    favoritePortFactory: (FavoriteRepository) -> FavoriteStatePort
) {
    progress = true
    runCatching { dataSources.getAllFavorites() }
        .mapCatching { it.map(favoritePortFactory) }
        .onFailure { error = it }
        .onSuccess { favorites.clear(); favorites.addAll(it) }
    progress = false
}