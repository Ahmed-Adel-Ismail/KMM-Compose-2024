package navigation.core

import navigation.core.ports.NavigationStatePort

suspend fun NavigationStatePort.initialize() {
    if (dataSourcePort.isLoggedIn() || true) screen.value = Screens.Home // TODO: revert after finishing home
    else screen.value = Screens.Login
}