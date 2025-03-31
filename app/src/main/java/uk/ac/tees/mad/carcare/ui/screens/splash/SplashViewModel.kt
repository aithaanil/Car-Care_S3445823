package uk.ac.tees.mad.carcare.ui.screens.splash

import uk.ac.tees.mad.carcare.model.repository.AuthRepository
import uk.ac.tees.mad.carcare.ui.navigation.SubGraph
import uk.ac.tees.mad.carcare.ui.screens.CarCareAppViewModel

class SplashViewModel(
    private val authRepository: AuthRepository
) : CarCareAppViewModel() {

    fun onAppStart(openAndPopUp: (Any, Any) -> Unit) {
        if(authRepository.isSignedIn()){
            openAndPopUp(SubGraph.HomeGraph, SubGraph.SplashScreenGraph)
        } else {
            openAndPopUp(SubGraph.AuthGraph, SubGraph.SplashScreenGraph)
        }
    }
}