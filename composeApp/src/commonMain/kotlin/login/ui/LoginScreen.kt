package login.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import login.core.ports.LoginStatePort
import login.core.scenarios.InvalidPasswordException
import login.core.scenarios.InvalidUsernameException
import login.core.scenarios.login

@Composable
fun LoginScreen(
    state: LoginStatePort,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    val scope = rememberCoroutineScope()

    LoginScreenContent(
        userName = state.userName,
        password = state.password,
        progress = state.progress,
        result = state.result,
        onUsernameChanged = { state.userName = it },
        onPasswordChanged = { state.password = it },
        onLoginClicked = { scope.launch(dispatcher) { state.login() } },
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
        val usernameFocusRequester = remember { FocusRequester() }
        val passwordFocusRequester = remember { FocusRequester() }

        Column(modifier = modifier.fillMaxSize()) {
            TopAppBar(title = { Text(text = "Login", modifier = Modifier.fillMaxWidth()) })
            if (progress) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
            ) {
                UsernameOutlinedTextField(
                    userName,
                    result,
                    usernameFocusRequester,
                    passwordFocusRequester,
                    onUsernameChanged
                )
                PasswordOutlinedTextField(
                    password,
                    result,
                    passwordFocusRequester,
                    onPasswordChanged,
                    onLoginClicked
                )
                LoginButton(progress, onLoginClicked)
            }
        }

        LaunchedEffect(Unit) {
            usernameFocusRequester.requestFocus()
        }
    }
}

@Composable
private fun UsernameOutlinedTextField(
    userName: String?,
    result: LoginStatePort.Result?,
    usernameFocusRequester: FocusRequester,
    passwordFocusRequester: FocusRequester,
    onUsernameChanged: (String?) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier
            .focusRequester(usernameFocusRequester)
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 16.dp),
        value = userName.orEmpty(),
        singleLine = true,
        label = { Text(text = "Username") },
        isError = result is LoginStatePort.Result.Error && result.error is InvalidUsernameException,
        onValueChange = onUsernameChanged,
        keyboardActions = KeyboardActions(onDone = { passwordFocusRequester.requestFocus() })
    )
}

@Composable
private fun PasswordOutlinedTextField(
    password: String?,
    result: LoginStatePort.Result?,
    focusRequester: FocusRequester,
    onPasswordChanged: (String?) -> Unit,
    onLoginClicked: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    OutlinedTextField(
        modifier = Modifier
            .focusRequester(focusRequester)
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 16.dp),
        value = password.orEmpty(),
        label = { Text(text = "Password") },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        isError = result is LoginStatePort.Result.Error && result.error is InvalidPasswordException,
        onValueChange = onPasswordChanged,
        keyboardActions = KeyboardActions(onDone = {
            focusManager.clearFocus()
            onLoginClicked()
        })
    )
}

@Composable
private fun LoginButton(progress: Boolean, onLoginClicked: () -> Unit) {
    val focusManager = LocalFocusManager.current
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(bottom = 16.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = if (!progress) MaterialTheme.colors.primary else Color.Gray),
        onClick = {
            if (!progress) {
                onLoginClicked()
                focusManager.clearFocus()
            }
        }) {
        Text(text = "Login")
    }
}