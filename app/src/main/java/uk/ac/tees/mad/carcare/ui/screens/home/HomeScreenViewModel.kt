package uk.ac.tees.mad.carcare.ui.screens.home

import uk.ac.tees.mad.carcare.model.repository.AuthRepository
import uk.ac.tees.mad.carcare.ui.screens.CarCareAppViewModel

class HomeScreenViewModel(
    private val authRepository: AuthRepository
): CarCareAppViewModel() {
    fun logOut() {
        authRepository.signOut()
    }
}