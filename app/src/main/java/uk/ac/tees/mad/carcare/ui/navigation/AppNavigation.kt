package uk.ac.tees.mad.carcare.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import uk.ac.tees.mad.carcare.ui.screens.login.LogInScreen
import uk.ac.tees.mad.carcare.ui.screens.splash.SplashScreen

fun NavGraphBuilder.CarCareGraph(appState: CarCareAppState) {
    navigation<SubGraph.SplashScreenGraph>(startDestination = Dest.SplashScreen) {
        composable<Dest.SplashScreen>{
            SplashScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
        }
    }
    navigation<SubGraph.AuthGraph>(startDestination = Dest.LogInScreen) {
        composable<Dest.LogInScreen>{
            LogInScreen()
        }
    }

}