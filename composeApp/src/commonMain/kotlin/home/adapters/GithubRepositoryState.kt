package home.adapters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import home.core.entities.GithubRepository
import home.core.ports.GithubRepositoryDataSourcesPort
import home.core.ports.GithubRepositoryStatePort

class GithubRepositoryState(
    githubRepository: GithubRepository,
    override val dataSourcePort: GithubRepositoryDataSourcesPort = GithubRepositoryDataSources()
) : GithubRepositoryStatePort {
    override var isFavorite by mutableStateOf(false)
    override val repository by mutableStateOf(githubRepository)
    override var progress by mutableStateOf(false)
    override var error by mutableStateOf<Throwable?>(null)
}