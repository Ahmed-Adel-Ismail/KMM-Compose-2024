package home.adapters

import data.DataSources
import data.DataSourcesImpl
import home.core.entities.GithubRepository
import home.core.ports.HomeDataSourcesPort

class HomeDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : HomeDataSourcesPort {
    override suspend fun getAllRepositories(): List<GithubRepository> {
        return dataSources.getAllGithubRepositories().data.map { createGithubRepository(it) }
    }

    override suspend fun getAllFavorites(): List<GithubRepository> {
        return dataSources.getAllFavorites().map { createGithubRepository(it) }
    }
}