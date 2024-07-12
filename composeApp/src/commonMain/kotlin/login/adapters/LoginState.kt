package login.adapters

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import login.core.ports.LoginDataSourcePort
import login.core.ports.LoginStatePort

class LoginState(
    override val dataSourcePort: LoginDataSourcePort = LoginDataSource()
) : ViewModel(), LoginStatePort {
    override val userName = mutableStateOf<String?>(null)
    override val password = mutableStateOf<String?>(null)
    override val progress = mutableStateOf(false)
    override val result = mutableStateOf<LoginStatePort.Result?>(null)
}