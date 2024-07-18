package home.adapters

import data.DataSources
import data.DataSourcesImpl
import home.core.entities.GithubRepository
import home.core.ports.GithubRepositoryDataSourcesPort

class GithubRepositoryDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : GithubRepositoryDataSourcesPort {

    override suspend fun addToFavorites(repository: GithubRepository) {
        dataSources.addToFavorites(repository.githubRepositoryData)
    }

    override suspend fun removeFromFavorites(repository: GithubRepository) {
        dataSources.removeFromFavorites(repository.githubRepositoryData)
    }
}