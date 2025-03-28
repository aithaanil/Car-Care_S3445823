package uk.ac.tees.mad.carcare

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.carcare.ui.navigation.CarCareAppState
import uk.ac.tees.mad.carcare.ui.navigation.CarCareGraph
import uk.ac.tees.mad.carcare.ui.navigation.SubGraph
import uk.ac.tees.mad.carcare.ui.theme.CarCareTheme

@Composable
fun CarCareApp() {
    CarCareTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            Scaffold { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = SubGraph.SplashScreenGraph,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    CarCareGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        CarCareAppState(navController)
    }