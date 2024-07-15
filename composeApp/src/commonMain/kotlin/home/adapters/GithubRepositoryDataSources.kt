package home.adapters

import GithubRepositoryData
import data.DataSources
import data.DataSourcesImpl
import home.core.entities.GithubRepository
import home.core.ports.GithubRepositoryDataSourcesPort

class GithubRepositoryDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : GithubRepositoryDataSourcesPort {

    override suspend fun addToFavorites(repository: GithubRepository) {
        dataSources.addToFavorites(repository.metadata as GithubRepositoryData)
    }

    override suspend fun removeFromFavorites(repository: GithubRepository) {
        dataSources.removeFromFavorites(repository.metadata as GithubRepositoryData)
    }
}