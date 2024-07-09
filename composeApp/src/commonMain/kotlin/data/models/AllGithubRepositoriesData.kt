package data.models

import GithubRepositoryData
import kotlinx.serialization.Serializable

@Serializable
data class AllGithubRepositoriesData(val data: List<GithubRepositoryData>)