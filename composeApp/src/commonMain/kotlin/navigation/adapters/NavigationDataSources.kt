package navigation.adapters

import data.DataSources
import data.DataSourcesImpl
import navigation.core.ports.NavigationDataSourcePort

class NavigationDataSources(
    private val dataSources: DataSources = DataSourcesImpl
) : NavigationDataSourcePort {
    override suspend fun isLoggedIn(): Boolean = dataSources.isLoggedIn()
}