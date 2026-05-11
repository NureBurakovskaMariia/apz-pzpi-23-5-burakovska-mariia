package ua.nure.petshelter.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ua.nure.petshelter.ui.animals.details.DetailsScreen
import ua.nure.petshelter.ui.auth.login.LoginScreen
import ua.nure.petshelter.ui.auth.register.RegistrationScreen

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Screen.Auth.SignIn
    ) {
        composable<Screen.Auth.Registration> {
            RegistrationScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
        composable<Screen.Auth.SignIn> {
            LoginScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Animals.Details> {
            DetailsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        composable<Screen.Donations> {
            DetailsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }

        animalsGraph(navController = navController)

        profileGraph(navController = navController)
        donationsGraph(navController = navController)
    }

}