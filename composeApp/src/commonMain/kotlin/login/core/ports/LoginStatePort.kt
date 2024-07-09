package login.core.ports

import androidx.compose.runtime.MutableState

interface LoginStatePort {
    val dataSourcePort: LoginDataSourcePort
    val progress: MutableState<Boolean>
    val userName: MutableState<String?>
    val password: MutableState<String?>
    val result: MutableState<Result?>

    sealed interface Result {
        data object Success : Result
        data class Error(val error: Throwable) : Result
    }
}