package login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import login.core.InvalidPasswordException
import login.core.InvalidUsernameException
import login.core.performLogin
import login.core.ports.LoginStatePort

@Composable
fun LoginScreen(
    loginStatePort: LoginStatePort,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val scope = rememberCoroutineScope()
    val username by loginStatePort.userName
    val password by loginStatePort.password
    val progress by loginStatePort.progress
    val result by loginStatePort.result

    LoginScreenContent(
        userName = username,
        password = password,
        progress = progress,
        result = result,
        onUsernameChanged = { loginStatePort.userName.value = it },
        onPasswordChanged = { loginStatePort.password.value = it },
        onLoginClicked = { scope.launch(dispatcher) { loginStatePort.performLogin() } },
        onSuccess = onSuccess,
        modifier = modifier
    )
}


@Composable
fun LoginScreenContent(
    userName: String?,
    password: String?,
    progress: Boolean,
    result: LoginStatePort.Result?,
    onUsernameChanged: (String?) -> Unit,
    onPasswordChanged: (String?) -> Unit,
    onLoginClicked: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (result == LoginStatePort.Result.Success) {
        onSuccess()
    } else {
        Column(modifier = modifier.fillMaxSize()) {
            TopAppBar(title = { Text(text = "Login", modifier = Modifier.fillMaxWidth()) })
            if (progress) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            ) {
                UsernameOutlinedTextField(userName, result, onUsernameChanged)
                PasswordOutlinedTextField(password, result, onPasswordChanged)
                LoginButton(progress, onLoginClicked)
            }
        }
    }
}

@Composable
fun UsernameOutlinedTextField(
    userName: String?,
    result: LoginStatePort.Result?,
    onUsernameChanged: (String?) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = userName.orEmpty(),
        singleLine = true,
        label = { Text(text = "Username") },
        isError = result is LoginStatePort.Result.Error && result.error is InvalidUsernameException,
        onValueChange = onUsernameChanged
    )
}

@Composable
fun PasswordOutlinedTextField(
    password: String?,
    result: LoginStatePort.Result?,
    onPasswordChanged: (String?) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        value = password.orEmpty(),
        label = { Text(text = "Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        isError = result is LoginStatePort.Result.Error && result.error is InvalidPasswordException,
        onValueChange = onPasswordChanged
    )
}

@Composable
fun LoginButton(progress: Boolean, onLoginClicked: () -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (!progress) MaterialTheme.colors.primary else Color.Gray),
        onClick = { if (!progress) onLoginClicked() }) {
        Text(text = "Login")
    }
}