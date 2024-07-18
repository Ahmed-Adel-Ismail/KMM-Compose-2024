package favorites.adapters

import GithubRepositoryData
import favorites.core.entities.FavoriteRepository

internal fun createFavoriteRepository(data: GithubRepositoryData) = FavoriteRepository(
    metadata = data,
    id = data.id ?: throw IllegalArgumentException("Missing GithubRepositoryData id"),
    name = data.name,
    ownerName = data.owner?.name,
    avatarUrl = data.owner?.avatarUrl,
    stargazersCount = data.stargazersCount,
)

val FavoriteRepository.githubRepositoryData: GithubRepositoryData
    get() = metadata as GithubRepositoryData