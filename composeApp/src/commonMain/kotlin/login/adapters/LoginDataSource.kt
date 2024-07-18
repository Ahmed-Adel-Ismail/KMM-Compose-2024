package login.adapters

import data.DataSources
import data.DataSourcesImpl
import login.core.User
import login.core.ports.LoginDataSourcePort

class LoginDataSource(
    private val dataSources: DataSources = DataSourcesImpl
) : LoginDataSourcePort {
    override suspend fun getUsernameValidation() = dataSources.getUsernameValidation()
    override suspend fun getPasswordValidations() = dataSources.getPasswordValidations()
    override suspend fun saveUser(user: User) = dataSources.saveUser(user.userData)
    override suspend fun login(username: String?, password: String?) =
        createUser(dataSources.postLogin(username, password))
}