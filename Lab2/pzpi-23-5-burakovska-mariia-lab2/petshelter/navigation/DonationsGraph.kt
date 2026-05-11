package ua.nure.petshelter.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.nure.petshelter.ui.donations.DonationsScreen
import ua.nure.petshelter.ui.profile.ProfileScreen

fun NavGraphBuilder.donationsGraph(navController: NavController) {
    navigation<NestedGraph.Donations>(
        startDestination = Screen.Donations
    ) {
        composable<Screen.Donations> {
            DonationsScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}