package home.core

data class SelectableGithubRepository(
    val isFavorite: Boolean,
    val repository: GithubRepository,
    val progress: Boolean = false,
    val error: Throwable? = null,
)