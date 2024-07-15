package navigation.core

import navigation.core.ports.NavigationStatePort

suspend fun NavigationStatePort.initialize() {
    if (dataSourcePort.isLoggedIn()) screen.value = Screens.Home
    else screen.value = Screens.Login
}