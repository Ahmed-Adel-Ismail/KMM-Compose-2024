package navigation.adapters

import androidx.compose.runtime.mutableStateOf
import navigation.core.Screens
import navigation.core.ports.NavigationDataSourcePort
import navigation.core.ports.NavigationStatePort

class NavigationState(
    override val dataSourcePort: NavigationDataSourcePort = NavigationDataSources(),
) : NavigationStatePort {
    override val screen = mutableStateOf<Screens>(Screens.Splash)
    override val state = mutableStateOf<Any?>(null)
}