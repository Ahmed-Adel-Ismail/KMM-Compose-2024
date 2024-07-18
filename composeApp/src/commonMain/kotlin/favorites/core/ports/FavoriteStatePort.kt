package favorites.core.ports

import favorites.core.entities.FavoriteRepository

interface FavoriteStatePort {
    val dataSources: FavoriteDataSourcesPort
    val favoriteRepository: FavoriteRepository
    var progress: Boolean
    var error: Throwable?
}