package navigation.core.ports

import androidx.compose.runtime.MutableState
import navigation.core.Screens
import shared.adapters.StateHolder

interface NavigationStatePort {
    val dataSourcePort: NavigationDataSourcePort
    val screen: MutableState<Screens>
    val state: MutableState<StateHolder<*>?>
}