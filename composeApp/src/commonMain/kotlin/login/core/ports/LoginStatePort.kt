package login.core.ports

interface LoginStatePort {
    val dataSourcePort: LoginDataSourcePort
    var progress: Boolean
    var userName: String?
    var password: String?
    var result: Result?

    sealed interface Result {
        data object Success : Result
        data class Error(val error: Throwable) : Result
    }
}