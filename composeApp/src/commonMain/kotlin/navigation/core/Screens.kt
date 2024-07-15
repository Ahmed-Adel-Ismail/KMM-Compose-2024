package navigation.core

sealed interface Screens {
    data object Splash : Screens
    data object Login : Screens
    data object Home : Screens
    data object Favorites : Screens
}