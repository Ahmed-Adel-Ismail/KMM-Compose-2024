import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OwnerData(
    @SerialName("id") val id: Long? = null,
    @SerialName("private") val isPrivate: Boolean? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null
)