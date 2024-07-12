package home.adapters

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import home.core.GithubRepository
import home.core.SelectableGithubRepository
import home.core.ports.HomeDataSourcesPort
import home.core.ports.HomeStatePort

class HomeState(
    override val dataSourcePort: HomeDataSourcesPort = HomeDataSources()
) : HomeStatePort {
    override val progress = mutableStateOf(false)
    override val genericError = mutableStateOf<Throwable?>(null)
    override val repositories = mutableStateListOf<GithubRepository>()
    override val favorites = mutableStateListOf<GithubRepository>()
    override val selectableRepositories = mutableStateListOf<SelectableGithubRepository>()
}