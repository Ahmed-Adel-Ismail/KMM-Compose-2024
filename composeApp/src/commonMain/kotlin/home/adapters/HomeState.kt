package home.adapters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import home.core.ports.GithubRepositoryStatePort
import home.core.ports.HomeDataSourcesPort
import home.core.ports.HomeStatePort

class HomeState(
    override val dataSourcePort: HomeDataSourcesPort = HomeDataSources()
) : HomeStatePort {
    override var progress by mutableStateOf(false)
    override var error by mutableStateOf<Throwable?>(null)
    override val repositories = mutableStateListOf<GithubRepositoryStatePort>()
}