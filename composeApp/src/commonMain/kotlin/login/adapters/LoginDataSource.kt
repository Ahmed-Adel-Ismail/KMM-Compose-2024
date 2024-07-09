package login.adapters

import kotlinx.coroutines.delay
import login.core.ports.LoginDataSourcePort

class LoginDataSource : LoginDataSourcePort {
    override suspend fun getUsernameValidation(): List<(String?) -> Boolean> {
        delay(3000) // simulate server delay
        return listOf(
            { it != null && it.length > 4 },
            { it?.contains("@") == true },
        )
    }

    override suspend fun getPasswordValidations(): List<(String?) -> Boolean> {
        delay(3000) // simulate server delay
        return listOf(
            { it != null && it.length > 4 }
        )
    }
}