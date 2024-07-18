package favorites.adapters

import data.DataSources
import data.DataSourcesImpl
import favorites.core.entities.FavoriteRepository
import favorites.core.ports.AllFavoritesDataSourcesPort

class AllFavoritesDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : AllFavoritesDataSourcesPort {
    override suspend fun getAllFavorites(): List<FavoriteRepository> {
        return dataSources.getAllFavorites().map { createFavoriteRepository(it) }
    }
}