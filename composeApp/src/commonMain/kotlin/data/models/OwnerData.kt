import kotlinx.serialization.Serializable

@Serializable
data class OwnerData(
    val id: Long? = null,
    val isPrivate: Boolean? = null,
    val name: String? = null,
    val avatarUrl: String? = null
)