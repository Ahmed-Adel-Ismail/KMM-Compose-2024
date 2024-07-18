package favorites.core.ports

import favorites.core.entities.FavoriteRepository

interface AllFavoritesDataSourcesPort {
    suspend fun getAllFavorites(): List<FavoriteRepository>
}
