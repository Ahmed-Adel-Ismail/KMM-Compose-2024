import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import favorites.ui.FavoritesScreen
import home.adapters.HomeState
import home.ui.HomeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import login.adapters.LoginState
import login.ui.LoginScreen
import navigation.adapters.NavigationState
import navigation.core.Screens.Favorites
import navigation.core.Screens.Home
import navigation.core.Screens.Login
import navigation.core.Screens.Splash
import navigation.core.initialize
import org.jetbrains.compose.ui.tooling.preview.Preview
import shared.adapters.StateHolder
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
    navigationState: NavigationState,
) {
    when (navigationState.screen) {

        Splash -> {
            navigationState.state = null
            SplashScreen()
        }

        Login -> {
            val holder = StateHolder("LoginState", LoginState())
            navigationState.state = holder
            LoginScreen(
                loginState = holder.state(),
                onSuccess = {
                    holder.clear()
                    navigationState.screen = Home
                },
            )
        }

        Home -> {
            val holder = StateHolder("HomeState", HomeState())
            navigationState.state = holder
            HomeScreen(
                state = holder.state(),
                onFavoritesClicked = { navigationState.screen = Favorites },
            )
        }

        Favorites -> {
            FavoritesScreen()
        }
    }
}





