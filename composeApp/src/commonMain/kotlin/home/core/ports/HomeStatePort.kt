package home.core.ports

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import home.core.GithubRepository
import home.core.SelectableGithubRepository

interface HomeStatePort {
    val dataSourcePort: HomeDataSourcesPort
    val progress: MutableState<Boolean>
    val repositories: SnapshotStateList<GithubRepository>
    val favorites: SnapshotStateList<GithubRepository>
    val selectableRepositories: SnapshotStateList<SelectableGithubRepository>
    val genericError: MutableState<Throwable?>
}