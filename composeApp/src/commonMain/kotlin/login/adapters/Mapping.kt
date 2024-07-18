package login.adapters

import data.models.UserData
import login.core.User

fun createUser(userData: UserData) = User(
    metadata = userData,
    id = userData.id ?: throw IllegalArgumentException("User id is required"),
    token = userData.token ?: throw IllegalArgumentException("User token is required"),
)

val User.userData: UserData
    get() = metadata as UserData