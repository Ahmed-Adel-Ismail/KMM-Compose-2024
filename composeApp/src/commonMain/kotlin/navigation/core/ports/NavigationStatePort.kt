package navigation.core.ports

import androidx.compose.runtime.MutableState
import navigation.core.Screens

interface NavigationStatePort {
    val dataSourcePort: NavigationDataSourcePort
    val screen: MutableState<Screens>
    val state: MutableState<Any?>
}