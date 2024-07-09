package login.core.ports

interface LoginDataSourcePort {
    suspend fun getUsernameValidation(): List<(String?) -> Boolean>
    suspend fun getPasswordValidations(): List<(String?) -> Boolean>
}