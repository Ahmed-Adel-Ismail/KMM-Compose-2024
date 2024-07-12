import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GithubRepositoryData(
    @SerialName("id") val id: Long? = null,
    @SerialName("private") val isPrivate: Boolean? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("stargazers_count") val stargazersCount: Int? = null,
    @SerialName("languages_url") val languagesUrl: String? = null,
    @SerialName("owner") val owner: OwnerData? = null
)