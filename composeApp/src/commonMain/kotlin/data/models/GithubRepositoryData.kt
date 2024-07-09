import kotlinx.serialization.Serializable

@Serializable
data class GithubRepositoryData(
    val id: Long? = null,
    val isPrivate: Boolean? = null,
    val name: String? = null,
    val description: String? = null,
    val stargazersCount: Int? = null,
    val languagesUrl: String? = null,
    val owner: OwnerData? = null
)