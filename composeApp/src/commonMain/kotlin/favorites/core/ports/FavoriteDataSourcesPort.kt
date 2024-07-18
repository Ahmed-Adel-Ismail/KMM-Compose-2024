package favorites.core.ports

import favorites.core.entities.FavoriteRepository

interface FavoriteDataSourcesPort {
    suspend fun removeFromFavorites(favoriteRepository: FavoriteRepository)
}