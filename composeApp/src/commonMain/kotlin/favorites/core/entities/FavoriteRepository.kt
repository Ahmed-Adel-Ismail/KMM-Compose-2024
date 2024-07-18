package favorites.core.entities

data class FavoriteRepository(
    val metadata: Any,
    val id: Long,
    val name: String? = null,
    val ownerName: String? = null,
    val avatarUrl: String? = null,
    val stargazersCount: Int? = null,
)