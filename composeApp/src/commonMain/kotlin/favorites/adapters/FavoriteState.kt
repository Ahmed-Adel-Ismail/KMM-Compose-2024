package favorites.adapters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import favorites.core.entities.FavoriteRepository
import favorites.core.ports.FavoriteDataSourcesPort
import favorites.core.ports.FavoriteStatePort

class FavoriteState(
    override val favoriteRepository: FavoriteRepository,
    override val dataSources: FavoriteDataSourcesPort = FavoriteDataSources()
) : FavoriteStatePort {
    override var progress by mutableStateOf(false)
    override var error: Throwable? by mutableStateOf(null)
}