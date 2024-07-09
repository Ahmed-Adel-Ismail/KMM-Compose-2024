package login.core

import login.core.ports.LoginStatePort
import login.core.ports.LoginStatePort.Result.Error
import login.core.ports.LoginStatePort.Result.Success

suspend fun LoginStatePort.performLogin() {
    progress.value = true
    validate(userName.value, password.value)
        .mapCatching { dataSourcePort.login(it.first, it.second) }
        .mapCatching { dataSourcePort.saveToken(it) }
        .onSuccess { result.value = Success }
        .onFailure { result.value = Error(it) }
    progress.value = false
}

private suspend fun LoginStatePort.validate(
    userName: String?,
    password: String?
) = runCatching {
    if (!dataSourcePort.getUsernameValidation().all { validation -> validation(userName) }) {
        throw InvalidUsernameException
    }
    if (!dataSourcePort.getPasswordValidations().all { validation -> validation(password) }) {
        throw InvalidPasswordException
    }
    userName to password
}

// in real life, more errors to be handled, like generic and network errors
object InvalidUsernameException : Exception()
object InvalidPasswordException : Exception()