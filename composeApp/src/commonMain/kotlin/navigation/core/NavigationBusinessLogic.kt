package navigation.core

import navigation.core.ports.NavigationStatePort

suspend fun NavigationStatePort.initialize() {
    screen = if (dataSourcePort.isLoggedIn()) Screens.Home else Screens.Login
}