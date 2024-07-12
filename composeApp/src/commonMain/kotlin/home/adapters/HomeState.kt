package home.adapters

import androidx.compose.runtime.mutableStateOf
import home.core.GithubRepository
import home.core.ports.HomeDataSourcesPort
import home.core.ports.HomeStatePort

class HomeState(
    override val dataSourcePort: HomeDataSourcesPort = HomeDataSources()
) : HomeStatePort {
    override val progress = mutableStateOf<Boolean>(false)
    override val repositories = mutableStateOf<List<GithubRepository>?>(null)
    override val error = mutableStateOf<Throwable?>(null)
}