package uk.ac.tees.mad.carcare.ui.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController

@Stable
class CarCareAppState(val navController: NavHostController) {
    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: Any) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: Any, popUp: Any) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: Any) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}