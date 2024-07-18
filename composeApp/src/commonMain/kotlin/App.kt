import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import favorites.adapters.AllFavoritesState
import favorites.ui.FavoritesScreen
import home.adapters.HomeState
import home.ui.HomeScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import login.adapters.LoginState
import login.ui.LoginScreen
import navigation.OnBackPressedChannel
import navigation.Provider
import navigation.adapters.NavigationState
import navigation.core.Screens.Favorites
import navigation.core.Screens.Home
import navigation.core.Screens.Login
import navigation.core.Screens.Splash
import navigation.core.initialize
import org.jetbrains.compose.ui.tooling.preview.Preview
import shared.adapters.StateHolder
import splash.SplashScreen

private val backChannelProvider = Provider(OnBackPressedChannel())

@Composable
@Preview
fun App(onBackPressedProvider: @Composable (Provider<OnBackPressedChannel>) -> Unit = {}) {
    MaterialTheme {
        Surface(color = MaterialTheme.colors.background) {
            onBackPressedProvider(backChannelProvider)
            val scope = rememberCoroutineScope()
            val navigationState by remember { mutableStateOf(NavigationState()) }
            LaunchedEffect(Unit) {
                scope.launch(Dispatchers.IO) {
                    navigationState.initialize()
                }
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
            backChannelProvider.value.onBackPressed = null
            navigationState.state = null
            SplashScreen()
        }

        Login -> {
            backChannelProvider.value.onBackPressed = null
            val holder = StateHolder("LoginState", LoginState())
            navigationState.state = holder
            LoginScreen(
                state = holder.state(),
                onSuccess = {
                    holder.clear()
                    navigationState.screen = Home
                },
            )
        }

        Home -> {
            backChannelProvider.value.onBackPressed = null
            val holder = StateHolder("HomeState", HomeState())
            navigationState.state = holder
            HomeScreen(
                state = holder.state(),
                onFavoritesClicked = { navigationState.screen = Favorites },
            )
        }

        Favorites -> {
            val holder = StateHolder("AllFavoritesState", AllFavoritesState())
            navigationState.state = holder
            backChannelProvider.value.onBackPressed = {
                holder.clear()
                navigationState.screen = Home
            }
            FavoritesScreen(
                state = holder.state(),
                onBackPress = {
                    backChannelProvider.value.onBackPressed?.invoke()
                }
            )
        }
    }
}





