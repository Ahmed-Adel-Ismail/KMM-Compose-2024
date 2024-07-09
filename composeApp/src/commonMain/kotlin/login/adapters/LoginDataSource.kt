package login.adapters

import data.DataSources
import data.DataSourcesImpl
import login.core.ports.LoginDataSourcePort

class LoginDataSource(
    private val dataSources: DataSources = DataSourcesImpl
) : LoginDataSourcePort {
    override suspend fun getUsernameValidation() = dataSources.getUsernameValidation()
    override suspend fun getPasswordValidations() = dataSources.getPasswordValidations()
    override suspend fun saveToken(token: String) = dataSources.saveToken(token)
    override suspend fun login(username: String?, password: String?) =
        dataSources.postLogin(username, password)
}