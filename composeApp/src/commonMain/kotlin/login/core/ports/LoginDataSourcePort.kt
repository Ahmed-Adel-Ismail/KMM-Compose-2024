package login.core.ports

import login.core.User

interface LoginDataSourcePort {
    suspend fun getUsernameValidation(): List<(String?) -> Boolean>
    suspend fun getPasswordValidations(): List<(String?) -> Boolean>
    suspend fun login(username: String?, password: String?): User
    suspend fun saveUser(user: User)
}