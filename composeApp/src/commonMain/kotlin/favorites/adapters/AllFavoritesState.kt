package favorites.adapters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import favorites.core.ports.AllFavoritesDataSourcesPort
import favorites.core.ports.AllFavoritesStatePort
import favorites.core.ports.FavoriteStatePort
import shared.adapters.Clearable

class AllFavoritesState(
    override val dataSources: AllFavoritesDataSourcesPort = AllFavoritesDataSources()
) : AllFavoritesStatePort, Clearable {
    override var favorites = mutableStateListOf<FavoriteStatePort>()
    override var progress by mutableStateOf(false)
    override var error by mutableStateOf<Throwable?>(null)
    override fun clear() {
        favorites.clear()
    }
}