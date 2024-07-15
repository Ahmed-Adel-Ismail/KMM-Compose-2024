package home.adapters

import GithubRepositoryData
import home.core.entities.GithubRepository

internal fun createGithubRepository(data: GithubRepositoryData) = GithubRepository(
    metadata = data,
    id = data.id ?: throw IllegalArgumentException("Missing GithubRepositoryData id"),
    name = data.name,
    ownerName = data.owner?.name,
    avatarUrl = data.owner?.avatarUrl,
    stargazersCount = data.stargazersCount,
)