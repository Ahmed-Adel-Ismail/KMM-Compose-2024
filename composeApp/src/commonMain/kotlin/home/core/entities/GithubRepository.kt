package home.core.entities

data class GithubRepository(
    /**
     * holds the pojo received from data source to be used later on when
     * dealing with data sources layer in other operations
     */
    val metadata: Any,
    val id: Long,
    val name: String? = null,
    val ownerName: String? = null,
    val avatarUrl: String? = null,
    val stargazersCount: Int? = null,
)