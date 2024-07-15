package navigation.adapters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import navigation.core.Screens
import navigation.core.ports.NavigationDataSourcePort
import navigation.core.ports.NavigationStatePort
import shared.adapters.StateHolder

class NavigationState(
    override val dataSourcePort: NavigationDataSourcePort = NavigationDataSources(),
) : NavigationStatePort {
    override var screen by mutableStateOf<Screens>(Screens.Splash)
    override var state by mutableStateOf<StateHolder<*>?>(null)
}