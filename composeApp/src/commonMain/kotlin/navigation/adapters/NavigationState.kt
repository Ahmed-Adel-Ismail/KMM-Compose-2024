package navigation.adapters

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import navigation.core.Screens
import navigation.core.ports.NavigationDataSourcePort
import navigation.core.ports.NavigationStatePort

class NavigationState(
    override val dataSourcePort: NavigationDataSourcePort = NavigationDataSources(),
) : ViewModel(), NavigationStatePort {
    override val screen = mutableStateOf<Screens>(Screens.Splash)
    override val state = mutableStateOf<Any?>(null)
}