package login.core

data class User(
    val metadata: Any,
    val id: Long,
    val token: String,
)