package uk.ac.tees.mad.carcare.model.dataclass.firebase

data class SignInResult(
    val data: UserDataGoogle?,
    val errorMessage: String?
)

data class UserDataGoogle(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)