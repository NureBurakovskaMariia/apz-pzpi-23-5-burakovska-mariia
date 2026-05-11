package ua.nure.petshelter.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import ua.nure.petshelter.ui.animals.details.DetailsScreen
import ua.nure.petshelter.ui.animals.list.ListScreen

fun NavGraphBuilder.animalsGraph(navController: NavController) {
    navigation<NestedGraph.Animals>(
        startDestination = Screen.Animals.List
    ) {
        composable<Screen.Animals.List> {
            ListScreen(
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
    }
}