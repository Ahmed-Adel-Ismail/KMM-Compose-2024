package favorites.adapters

import data.DataSources
import data.DataSourcesImpl
import favorites.core.entities.FavoriteRepository
import favorites.core.ports.FavoriteDataSourcesPort

class FavoriteDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : FavoriteDataSourcesPort {
    override suspend fun removeFromFavorites(favoriteRepository: FavoriteRepository) {
        return dataSources.removeFromFavorites(favoriteRepository.githubRepositoryData)
    }
}