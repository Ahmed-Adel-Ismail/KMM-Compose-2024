package favorites.core.ports

interface AllFavoritesStatePort {
    val dataSources: AllFavoritesDataSourcesPort
    val favorites: MutableList<FavoriteStatePort>
    var progress: Boolean
    var error: Throwable?
}