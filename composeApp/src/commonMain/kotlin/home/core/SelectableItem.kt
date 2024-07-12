package home.core

data class SelectableGithubRepository(
    val isFavorite: Boolean,
    val repository: GithubRepository,
    val error: Throwable? = null,
)