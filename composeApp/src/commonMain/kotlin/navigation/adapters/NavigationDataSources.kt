package navigation.adapters

import dataSources.DataSources
import dataSources.DataSourcesImpl
import navigation.core.ports.NavigationDataSourcePort

class NavigationDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : NavigationDataSourcePort {
    override suspend fun isLoggedIn(): Boolean = dataSources.isLoggedIn()
}