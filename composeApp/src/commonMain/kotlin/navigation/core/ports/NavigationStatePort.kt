package navigation.core.ports

import navigation.core.Screens
import shared.adapters.StateHolder

interface NavigationStatePort {
    val dataSourcePort: NavigationDataSourcePort
    var screen: Screens
    var state: StateHolder<*>?
}