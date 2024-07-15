package home.core.ports

interface HomeStatePort {
    val dataSourcePort: HomeDataSourcesPort
    var progress: Boolean
    val repositories: MutableList<GithubRepositoryStatePort>
    var error: Throwable?
}