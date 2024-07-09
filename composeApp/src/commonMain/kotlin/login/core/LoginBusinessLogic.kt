package login.core

import androidx.annotation.VisibleForTesting
import login.core.ports.LoginDataSourcePort
import login.core.ports.LoginStatePort
import login.core.ports.LoginStatePort.Result.Error
import login.core.ports.LoginStatePort.Result.Success

suspend fun LoginStatePort.performLogin() {
    progress.value = true
    validate(dataSourcePort, userName.value, password.value)
        .onSuccess { result.value = Success }
        .onFailure { result.value = Error(it) }
    progress.value = false
}

@VisibleForTesting
internal suspend fun validate(
    dataSourcePort: LoginDataSourcePort,
    userName: String?,
    password: String?
) = runCatching {
    if (!dataSourcePort.getUsernameValidation().all { validation -> validation(userName) }) {
        throw InvalidUsernameException
    }
    if (!dataSourcePort.getPasswordValidations().all { validation -> validation(password) }) {
        throw InvalidPasswordException
    }
    true
}

object InvalidUsernameException : Exception()
object InvalidPasswordException : Exception()