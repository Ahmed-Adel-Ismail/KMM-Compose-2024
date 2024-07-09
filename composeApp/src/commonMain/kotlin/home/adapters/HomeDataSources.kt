package home.adapters

import data.DataSources
import data.DataSourcesImpl
import home.core.GithubRepository
import home.core.ports.HomeDataSourcesPort

class HomeDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : HomeDataSourcesPort {
    override suspend fun getAllRepositories(): List<GithubRepository> =
        dataSources.getAllGithubRepositories().data.map {
            GithubRepository(
                metadata = it,
                id = it.id,
                name = it.name,
                ownerName = it.owner?.name,
                avatarUrl = it.owner?.avatarUrl,
                stargazersCount = it.stargazersCount,
            )
        }
}