package uk.ac.tees.mad.carcare.model.dataclass.firebase

data class SignInState(
    val isSignInSuccessful: Boolean = false, val signInError: String? = null
)