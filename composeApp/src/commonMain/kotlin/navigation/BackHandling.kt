package navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlin.properties.Delegates

class Provider<T>(val value: T) {}

class OnBackPressedChannel {
    var isBackPressActive by mutableStateOf(false)
    var onBackPressed: (() -> Unit)? by Delegates.observable(null) { _, _, value ->
        isBackPressActive = value != null
    }
}
