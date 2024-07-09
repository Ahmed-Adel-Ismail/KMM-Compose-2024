package home.core.ports

import androidx.compose.runtime.MutableState
import home.core.GithubRepository

interface HomeStatePort {
    val dataSourcePort: HomeDataSourcesPort
    val progress: MutableState<Boolean>
    val repositories: MutableState<List<GithubRepository>?>
    val error: MutableState<Throwable?>
}