package login.adapters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import login.core.ports.LoginDataSourcePort
import login.core.ports.LoginStatePort

class LoginState(
    override val dataSourcePort: LoginDataSourcePort = LoginDataSource()
) : ViewModel(), LoginStatePort {
    override var userName by mutableStateOf<String?>(null)
    override var password by mutableStateOf<String?>(null)
    override var progress by mutableStateOf(false)
    override var result by mutableStateOf<LoginStatePort.Result?>(null)
}