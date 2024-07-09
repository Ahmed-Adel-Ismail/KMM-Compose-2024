import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import home.HomeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import login.LoginScreen
import login.adapters.LoginState
import login.core.ports.LoginStatePort
import navigation.adapters.NavigationState
import navigation.core.Screens.Home
import navigation.core.Screens.Login
import navigation.core.Screens.Splash
import navigation.core.initialize
import org.jetbrains.compose.ui.tooling.preview.Preview
import splash.SplashScreen

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface(color = MaterialTheme.colors.background) {
            val scope = rememberCoroutineScope()
            val navigationState by remember { mutableStateOf(NavigationState()) }
            LaunchedEffect(Unit) {
                scope.launch(Dispatchers.IO) { navigationState.initialize() }
            }
            Screen(navigationState)
        }
    }
}

@Composable
fun Screen(
    navigationState: NavigationState
) {
    val screen by navigationState.screen
    var state by navigationState.state

    when (screen) {

        Splash -> {
            state = null
            SplashScreen()
        }

        Login -> {
            val newState: LoginStatePort =
                if (state is LoginStatePort) state as LoginStatePort
                else LoginState().also { state = it }

            LoginScreen(
                loginStatePort = newState,
                onSuccess = { navigationState.screen.value = Home },
            )
        }

        Home -> {
            state = null
            HomeScreen()
        }
    }
}





