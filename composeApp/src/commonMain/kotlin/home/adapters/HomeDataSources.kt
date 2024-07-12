package home.adapters

import GithubRepositoryData
import data.DataSources
import data.DataSourcesImpl
import home.core.GithubRepository
import home.core.ports.HomeDataSourcesPort

class HomeDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : HomeDataSourcesPort {

    override suspend fun getAllRepositories(): List<GithubRepository> =
        dataSources.getAllGithubRepositories().data.map { createGithubRepository(it) }

    override suspend fun addToFavorites(repository: GithubRepository) {
        dataSources.addToFavorites(repository.metadata as GithubRepositoryData)
    }

    override suspend fun removeFromFavorites(repository: GithubRepository) {
        dataSources.removeFromFavorites(repository.metadata as GithubRepositoryData)
    }

    override suspend fun getAllFavorites(): List<GithubRepository> {
        return dataSources.getAllFavorites().map { createGithubRepository(it) }
    }

    private fun createGithubRepository(data: GithubRepositoryData) = GithubRepository(
        metadata = data,
        id = data.id,
        name = data.name,
        ownerName = data.owner?.name,
        avatarUrl = data.owner?.avatarUrl,
        stargazersCount = data.stargazersCount,
    )

}