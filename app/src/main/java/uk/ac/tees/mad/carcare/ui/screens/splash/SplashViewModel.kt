package uk.ac.tees.mad.carcare.ui.screens.splash

import uk.ac.tees.mad.carcare.ui.navigation.SubGraph
import uk.ac.tees.mad.carcare.ui.screens.CarCareAppViewModel

class SplashViewModel(

) : CarCareAppViewModel() {

    fun onAppStart(openAndPopUp: (Any, Any) -> Unit) {
//        if (accountService.hasUser()) openAndPopUp(NOTES_LIST_SCREEN, SPLASH_SCREEN)
//        else openAndPopUp(SIGN_IN_SCREEN, SPLASH_SCREEN)
        openAndPopUp(SubGraph.AuthGraph, SubGraph.SplashScreenGraph)
    }
}